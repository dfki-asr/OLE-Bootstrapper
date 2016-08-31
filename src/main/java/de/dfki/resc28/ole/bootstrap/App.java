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

// TODO: make Fuseki endpoint configurable
// TODO: make LDraw parts directory configurable
// TODO: rethink dataset layout!
public class App 
{
	public static void main( String[] args ) throws IOException
	{
//		InputStream fi = new FileInputStream("/Users/resc01/Desktop/ldraw/ldraw/parts/003238a.dat");
//		LDrawLexer lex = new LDrawLexer(new ANTLRInputStream(fi));
//		CommonTokenStream t = new CommonTokenStream(lex);
//		
//		t.fill();
//		
//		for (Token i : t.getTokens())
//			System.out.println(LDrawLexer.VOCABULARY.getSymbolicName(i.getType()) + ": " + i.getText());
		
		String dataEndpoint = "http://localhost:3030/ole/data";
		String queryEndpoint = "http://localhost:3030/ole/sparql";
		IGraphStore fGraphStore = new FusekiGraphStore(dataEndpoint, queryEndpoint);
		
//		IGraphStore fGraphStore = new TDBGraphStore("/Users/resc01/Desktop/ldraw_tdb");

		String partsDirectory = "/Users/resc01/Desktop/ldraw/ldraw/parts";
		
		File[] files = new File(partsDirectory).listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".dat");
		    }
		});
		
		
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
		repoModel.setNsPrefix("repo", "http://ole.dfki.de/repo/");
		repoModel.setNsPrefix("users", "http://ole.dfki.de/repo/users/");
		repoModel.setNsPrefix("assets", "http://ole.dfki.de/repo/assets/");
		repoModel.setNsPrefix("distributions", "http://ole.dfki.de/repo/distributions/");
		
		Resource repo = repoModel.createResource("http://ole.dfki.de/repo");
		repoModel.add( repo, RDF.type, ADMS.AssetRepository );
		repoModel.add( repo, DCTerms.title, "The Open Lego Parts Repository" );
		repoModel.add( repo, DCTerms.created, repoModel.createTypedLiteral(Calendar.getInstance().getTime(),XSDDatatype.XSDdate));
		repoModel.add( repo, DCTerms.description, "A complete catalog of LDraw parts" );
		repoModel.add( repo, DCTerms.publisher, "resc28" );
		
		

		int fileCounter = 0;
		
		for (File file : files) 
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
				
//				// create users
//				walker.walk(new UserListener(file.getName(), fGraphStore), tree);
				
				
				// create create ldraw-distribution
				walker.walk(new LdrawDistributionListener(file.getName(), fGraphStore), tree);
				
				
				// create ARVIDA distribution
				
				
				// add asset to repo
				Resource asset = repoModel.createResource("http://ole.dfki.de/repo/assets/" + FilenameUtils.getBaseName(file.getName()));
				repoModel.add( repo, DCAT.dataset, asset );
			
				// close InputStream
				fis.close();			
				fileCounter++;
				
				if (fileCounter == 100)
					break;
			}
		}
		
		// put repo model to graphStore
//		fGraphStore.addToDefaultGraph(repoModel);
		fGraphStore.addToNamedGraph(repo.getURI(), repoModel);
		
		
		System.out.println("Parsed assets: " + fileCounter);
		System.exit(0);
	}
	
	
}
