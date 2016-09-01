/*
 * This file is part of OLE. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */

package de.dfki.resc28.ole.bootstrap;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
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

public class App 
{
	public static String fBaseURI = null;
	public static String fPartsDirectory = null;
	public static IGraphStore fGraphStore = null;
	public static Model fRepoModel = null;
	public static Resource fRepo = null;


	public static synchronized void configure()
	{
		try 
		{
            String configFile = System.getProperty("bootstrap.configuration");
            java.io.InputStream is;
            
            if (configFile != null) 
            {
                is = new java.io.FileInputStream(configFile);
                System.out.format("Loading OLE Bootstrapper configuration from %s ...%n", configFile);
            } 
            else 
            {
                is = App.class.getClassLoader().getResourceAsStream("bootstrap.properties");
                System.out.println("Loading OLE Bootstrapper configuration from internal resource file ...");
            }
            
            java.util.Properties p = new Properties();
            p.load(is);

            fBaseURI = p.getProperty("baseURI");
            fPartsDirectory = p.getProperty("partsDirectory");
                       
            String storage = p.getProperty("graphStore");
            if (storage.equals("fuseki")) 
            {
                String dataEndpoint = p.getProperty("dataEndpoint");
                String queryEndpoint = p.getProperty("queryEndpoint");
                System.out.format("Use Fuseki backend: dataEndpoint=%s queryEndpoint=%s ...%n", dataEndpoint, queryEndpoint);

                fGraphStore = new FusekiGraphStore(dataEndpoint, queryEndpoint);
            }
            
            
		}
        catch (Exception e) 
		{
            e.printStackTrace();
        }
	}
	
	
	
	
	public static void main( String[] args ) throws IOException
	{
		configure();
		initRepoModel();

//		parseFile(new File("/Users/resc01/Desktop/ldraw/ldraw/parts/995.dat"));		
		
		File[] files = new File(fPartsDirectory).listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".dat");
		    }
		});
		
		for (File file : files) 
			parseFile(file);
		
		fGraphStore.addToNamedGraph(fRepo.getURI(), fRepoModel);

		System.exit(0);
	}
	
	
	
	
	private static void initRepoModel()
	{
		fRepoModel = ModelFactory.createDefaultModel();
		fRepoModel.setNsPrefixes(FOAF.NAMESPACE);
		fRepoModel.setNsPrefixes(ADMS.NAMESPACE);
		fRepoModel.setNsPrefixes(DCAT.NAMESPACE);
		fRepoModel.setNsPrefix("dcterms", DCTerms.NS);
		fRepoModel.setNsPrefix("rdf", RDF.getURI());
		fRepoModel.setNsPrefix("rdfs", RDFS.getURI());
		fRepoModel.setNsPrefix("skos", SKOS.getURI());
		fRepoModel.setNsPrefix("xsd", XSD.NS);
		fRepoModel.setNsPrefix("ldraw", "http://www.ldraw.org/ns/ldraw#");
		fRepoModel.setNsPrefix("repo", fBaseURI + "repo/");
		fRepoModel.setNsPrefix("users", fBaseURI + "repo/users/");
		fRepoModel.setNsPrefix("assets", fBaseURI + "repo/assets/");
		fRepoModel.setNsPrefix("distributions", fBaseURI + "repo/distributions/");
		
		fRepo = fRepoModel.createResource(fBaseURI + "repo");
		fRepoModel.add( fRepo, RDF.type, ADMS.AssetRepository );
		fRepoModel.add( fRepo, DCTerms.title, "The Open Lego Parts Repository" );
		fRepoModel.add( fRepo, DCTerms.created, fRepoModel.createTypedLiteral(Calendar.getInstance().getTime(),XSDDatatype.XSDdate));
		fRepoModel.add( fRepo, DCTerms.description, "A complete catalog of LDraw parts" );
		fRepoModel.add( fRepo, DCTerms.publisher, "resc28" );
		
		fGraphStore.createNamedGraph(fRepo.getURI(), fRepoModel);
	}
	
	
	private static void parseFile(File file) throws IOException
	{
		if (file.isFile()) 
		{	
			
			System.out.println(file.getAbsolutePath());
			
			// parse the .DAT file and create RDF models for asset and its .DAT distribution
			InputStream fis = new FileInputStream(file);
			LDrawLexer lexer = new LDrawLexer(new ANTLRInputStream(fis));
			LDrawParser parser = new LDrawParser(new CommonTokenStream(lexer));
			ParseTreeWalker walker = new ParseTreeWalker();
			ParseTree tree = parser.file();	
			
			// create asset
			walker.walk(new AssetListener(file.getName(), fGraphStore), tree);		
			
//			// create users
//			walker.walk(new UserListener(file.getName(), fGraphStore), tree);
			
			
			// create create ldraw-distribution
			walker.walk(new LdrawDistributionListener(file.getName(), fGraphStore), tree);
			
			
			// create ARVIDA distribution
			
			
			// add asset to repo
			Resource asset = fRepoModel.createResource(fBaseURI + "repo/assets/" + FilenameUtils.getBaseName(file.getName()));
			fRepoModel.add( fRepo, DCAT.dataset, asset );
		
			// close InputStream
			fis.close();			
		}
	}
}
