/*
 * This file is part of OLE. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */

package de.dfki.resc28.ole.bootstrap.listener;

import org.apache.commons.io.FilenameUtils;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;
import org.apache.jena.vocabulary.XSD;

import de.dfki.resc28.LDrawParser.LDrawParser.Author_rowContext;
import de.dfki.resc28.LDrawParser.LDrawParser.FileContext;
import de.dfki.resc28.LDrawParser.LDrawParserBaseListener;
import de.dfki.resc28.igraphstore.IGraphStore;
import de.dfki.resc28.ole.bootstrap.App;
import de.dfki.resc28.ole.bootstrap.Util;
import de.dfki.resc28.ole.bootstrap.vocabularies.ADMS;
import de.dfki.resc28.ole.bootstrap.vocabularies.DCAT;
import de.dfki.resc28.ole.bootstrap.vocabularies.FOAF;

public class LdrawDistributionListener extends LDrawParserBaseListener
{
	private Model distributionModel;
	private Resource distribution;
	private Resource asset;

	private String basename;
	private String fileName;

	private IGraphStore graphStore;
	
	
	
	public LdrawDistributionListener(String fileName, IGraphStore graphStore)
	{
		super();
		
		this.basename = FilenameUtils.getBaseName(fileName);
		this.graphStore = graphStore;
		this.fileName = fileName;
	}
	
	@Override
	public void enterFile(FileContext ctx) 
	{
		distributionModel = ModelFactory.createDefaultModel();
		distributionModel.setNsPrefixes(FOAF.NAMESPACE);
		distributionModel.setNsPrefixes(ADMS.NAMESPACE);
		distributionModel.setNsPrefixes(DCAT.NAMESPACE);
		distributionModel.setNsPrefix("dcterms", DCTerms.NS);
		distributionModel.setNsPrefix("rdf", RDF.getURI());
		distributionModel.setNsPrefix("rdfs", RDFS.getURI());
		distributionModel.setNsPrefix("skos", SKOS.getURI());
		distributionModel.setNsPrefix("xsd", XSD.NS);
		distributionModel.setNsPrefix("ldraw", "http://www.ldraw.org/ns/ldraw#");
		distributionModel.setNsPrefix("users", App.fUserBaseUri);
		distributionModel.setNsPrefix("assets", App.fAssetBaseUri);
		distributionModel.setNsPrefix("distributions", App.fDistributionBaseUri);

		asset = distributionModel.createResource(Util.joinPath(App.fAssetBaseUri, Util.urlEncoded(basename)));
		distributionModel.add( asset, RDF.type, ADMS.Asset);
		distribution = distributionModel.createResource(Util.joinPath(App.fDistributionBaseUri, Util.urlEncoded(basename)));
		distributionModel.add( distribution, RDF.type, ADMS.AssetDistribution );
		distributionModel.add( distribution, DCTerms.format, "application/x-ldraw" );
		distributionModel.add( distribution, DCAT.mediaType, "application/x-ldraw" );
		distributionModel.add( distribution, DCTerms.isReferencedBy, asset );
		Literal downloadURL = distributionModel.createTypedLiteral( Util.joinPath(App.fStorageURI, Util.urlEncoded(fileName)), XSDDatatype.XSDanyURI );
		distributionModel.add( distribution, DCAT.downloadURL, downloadURL);
	};

	@Override
	public void exitFile(FileContext ctx)
	{
		if (distribution != null)
		{
			graphStore.addToNamedGraph(distribution.getURI(), distributionModel);
		
			// slow down to prevent org.apache.jena.atlas.web.HttpException: 500 - Direct buffer memory
			try 
			{
				Thread.sleep(50);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public void exitAuthor_row(Author_rowContext ctx)
	{
		if (ctx != null)
		{
			if (ctx.realname() != null)
			{
				Resource creator = distributionModel.createResource(Util.joinPath(App.fUserBaseUri, Util.toURLEncodedStringLiteral(ctx.realname().STRING(), "_").toString()));
				distributionModel.add( distribution, FOAF.maker, creator );
				distributionModel.add( distribution,  DCTerms.creator, creator );
			}
		}
	}
	
//	@Override
//	public void exitName_row(Name_rowContext ctx)
//	{			  
//		if (ctx != null)
//		{
//			distributionModel.add( distribution, RDF.type, ADMS.AssetDistribution ) ;
//			distributionModel.add( distribution, DCTerms.format, "application/x-ldraw" );
//			distributionModel.add( distribution, DCAT.mediaType, "application/x-ldraw" );
//		}
//	}
	
//	@Override 
//	public void exitLdraw_row(Ldraw_rowContext ctx)
//	{
//		if (ctx != null)
//		{
//			String downloadURL ;
//			String accessURL = Util.appendSegmentToPath("http://www.ldraw.org/cgi-bin/ptdetail.cgi?f=parts/", Util.urlEncoded(fileName));
//			
//			if (ctx.type().TYPE().getText().contains("Unofficial_Part") | ctx.type().TYPE().getText().contains("Unofficial_Subpart") | ctx.type().TYPE().getText().contains("Unofficial_Sub-part"))
//			{
//				downloadURL = Util.appendSegmentToPath("http://www.ldraw.org/library/unofficial/parts/", Util.urlEncoded(basename));
//				distributionModel.add( distribution, DCAT.downloadURL, distributionModel.createTypedLiteral(downloadURL, XSDDatatype.XSDanyURI));
//				distributionModel.add( distribution, DCAT.accessURL, distributionModel.createTypedLiteral(accessURL, XSDDatatype.XSDanyURI));
//			}
//			else if (ctx.type().TYPE().getText().contains("Part") | ctx.type().TYPE().getText().contains("Subpart") | ctx.type().TYPE().getText().contains("Sub-part"))
//			{
//				downloadURL = Util.appendSegmentToPath("http://www.ldraw.org/library/unofficial/parts/", Util.urlEncoded(basename));
//				distributionModel.add( distribution, DCAT.downloadURL, distributionModel.createTypedLiteral(downloadURL, XSDDatatype.XSDanyURI));
//				distributionModel.add( distribution, DCAT.accessURL, distributionModel.createTypedLiteral(accessURL, XSDDatatype.XSDanyURI));
//			}
//		}
//	}
}
