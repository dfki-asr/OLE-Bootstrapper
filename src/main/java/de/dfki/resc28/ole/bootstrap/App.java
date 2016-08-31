/*
 * This file is part of OLE. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.ole.bootstrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.io.FilenameUtils;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;
import org.apache.jena.vocabulary.XSD;

import de.dfki.resc28.LDrawParser.*;
import de.dfki.resc28.igraphstore.IGraphStore;
import de.dfki.resc28.igraphstore.jena.FusekiGraphStore;
import de.dfki.resc28.ole.bootstrap.listener.AssetListener;
import de.dfki.resc28.ole.bootstrap.listener.LdrawDistributionListener;
import de.dfki.resc28.ole.bootstrap.vocabularies.ADMS;
import de.dfki.resc28.ole.bootstrap.vocabularies.DCAT;
import de.dfki.resc28.ole.bootstrap.vocabularies.FOAF;

// TODO: rethink dataset layout!
public class App {

    public static String fBaseURI = null;
    public static String fPartsDirectory = null;
    public static IGraphStore fGraphStore = null;

    public static String getProperty(java.util.Properties p, String key, String sysKey) {
        String value = System.getProperty(sysKey);
        if (value != null)
            return value;
        return p.getProperty(key);
    }

    public static synchronized void configure() {
        try {
            String configFile = System.getProperty("bootstrap.configuration");
            java.io.InputStream is;

            if (configFile != null) {
                is = new java.io.FileInputStream(configFile);
                System.out.format("Loading OLE Bootstrapper configuration from %s ...%n", configFile);
            } else {
                is = App.class.getClassLoader().getResourceAsStream("bootstrap.properties");
                System.out.println("Loading OLE Bootstrapper configuration from internal resource file ...");
            }

            java.util.Properties p = new Properties();
            p.load(is);

            fBaseURI = getProperty(p, "baseURI", "bootstrap.baseURI");
            fPartsDirectory = getProperty(p, "partsDirectory", "bootstrap.partsDirectory");

            String storage = getProperty(p, "graphStore", "bootstrap.graphStore");
            if (storage.equals("fuseki")) {
                String dataEndpoint = getProperty(p, "dataEndpoint", "bootstrap.dataEndpoint");
                String queryEndpoint = getProperty(p, "queryEndpoint", "bootstrap.queryEndpoint");
                System.out.format("Use Fuseki backend:%n  dataEndpoint=%s%n  queryEndpoint=%s ...%n", dataEndpoint, queryEndpoint);

                fGraphStore = new FusekiGraphStore(dataEndpoint, queryEndpoint);
            }

            // Overriders
            if (fPartsDirectory == null || !new File(fPartsDirectory).isDirectory()) {
                String ldrawDir = System.getenv("LDRAWDIR");
                if (ldrawDir != null) {
                    File dir = new File(ldrawDir);
                    if (dir.isDirectory()) {
                        dir = new File(dir, "parts");
                        if (dir.isDirectory())
                            fPartsDirectory = dir.toString();
                    }
                }
            }
            System.out.format("Use LDraw parts directory: %s%n", fPartsDirectory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        configure();

//		String partsDirectory = "/Users/resc01/Desktop/ldraw/ldraw/parts";
        File[] files = new File(fPartsDirectory).listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".dat");
            }
        });

        if (files == null) {
            System.err.format("Error: %s is not a directory%n", fPartsDirectory);
            System.exit(1);
        }

        Model repoModel = ModelFactory.createDefaultModel();
        repoModel.setNsPrefixes(FOAF.NAMESPACE);
        repoModel.setNsPrefixes(ADMS.NAMESPACE);
        repoModel.setNsPrefixes(DCAT.NAMESPACE);
        repoModel.setNsPrefix("dcterms", DCTerms.NS);
        repoModel.setNsPrefix("rdf", RDF.getURI());
        repoModel.setNsPrefix("rdfs", RDFS.getURI());
        repoModel.setNsPrefix("skos", SKOS.getURI());
        repoModel.setNsPrefix("xsd", XSD.NS);
        repoModel.setNsPrefix("ldraw", "http://www.ldraw.org/ns/ldraw#");
        repoModel.setNsPrefix("repo", fBaseURI + "/repo/");
        repoModel.setNsPrefix("users", fBaseURI + "/repo/users/");
        repoModel.setNsPrefix("assets", fBaseURI + "/repo/assets/");
        repoModel.setNsPrefix("distributions", fBaseURI + "/repo/distributions/");

        Resource repo = repoModel.createResource(fBaseURI + "/repo");
        repoModel.add(repo, RDF.type, ADMS.AssetRepository);
        repoModel.add(repo, DCTerms.title, "The Open Lego Parts Repository");
        repoModel.add(repo, DCTerms.created, repoModel.createTypedLiteral(Calendar.getInstance().getTime(), XSDDatatype.XSDdate));
        repoModel.add(repo, DCTerms.description, "A complete catalog of LDraw parts");
        repoModel.add(repo, DCTerms.publisher, "resc28");

        int fileCounter = 0;

        for (File file : files) {

            if (file.isFile()) {

                System.out.format("Parsing file: %s [%d/%d]...%n", file.getAbsolutePath(), fileCounter+1, files.length);

                // parse the .DAT file and create RDF models for asset and its .DAT distribution
                InputStream fis = new FileInputStream(file);
                LDrawLexer lexer = new LDrawLexer(new ANTLRInputStream(fis));
                LDrawParser parser = new LDrawParser(new CommonTokenStream(lexer));
                ParseTreeWalker walker = new ParseTreeWalker();
                ParseTree tree = parser.file();

                System.out.format("Create asset ...%n");
                // create asset
                walker.walk(new AssetListener(file.getName(), fGraphStore), tree);

//				// create users
//				walker.walk(new UserListener(file.getName(), fGraphStore), tree);
                // create create ldraw-distribution

                System.out.format("Create ldraw-distribution ...%n");
                walker.walk(new LdrawDistributionListener(file.getName(), fGraphStore), tree);

				// create ARVIDA distribution
                // add asset to repo
                System.out.format("Add asset to repo ...%n");
                Resource asset = repoModel.createResource(fBaseURI + "/repo/assets/" + FilenameUtils.getBaseName(file.getName()));
                repoModel.add(repo, DCAT.dataset, asset);

                // close InputStream
                fis.close();
                fileCounter++;

                System.out.format("Done%n");

                //if (fileCounter == 100) {
                //    break;
                //}
            }
        }

        // put repo model to graphStore
//		fGraphStore.addToDefaultGraph(repoModel);
        fGraphStore.addToNamedGraph(repo.getURI(), repoModel);

        System.out.println("Parsed assets: " + fileCounter);
        System.exit(0);
    }

}
