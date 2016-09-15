/*
 * This file is part of OLE. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */


package de.dfki.resc28.ole.bootstrap.listener;



import org.antlr.v4.runtime.tree.TerminalNode;
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
import de.dfki.resc28.LDrawParser.LDrawParser.Category_rowContext;
import de.dfki.resc28.LDrawParser.LDrawParser.Comment_rowContext;
import de.dfki.resc28.LDrawParser.LDrawParser.FileContext;
import de.dfki.resc28.LDrawParser.LDrawParser.Help_rowContext;
import de.dfki.resc28.LDrawParser.LDrawParser.History_rowContext;
import de.dfki.resc28.LDrawParser.LDrawParser.Keywords_rowContext;
import de.dfki.resc28.LDrawParser.LDrawParser.Ldraw_rowContext;
import de.dfki.resc28.LDrawParser.LDrawParser.License_rowContext;
import de.dfki.resc28.LDrawParser.LDrawParser.Reference_rowContext;
import de.dfki.resc28.LDrawParser.LDrawParser.TitleContext;
import de.dfki.resc28.LDrawParser.LDrawParserBaseListener;
import de.dfki.resc28.igraphstore.IGraphStore;
import de.dfki.resc28.ole.bootstrap.App;
import de.dfki.resc28.ole.bootstrap.Util;
import de.dfki.resc28.ole.bootstrap.vocabularies.ADMS;
import de.dfki.resc28.ole.bootstrap.vocabularies.DCAT;
import de.dfki.resc28.ole.bootstrap.vocabularies.FOAF;




public class AssetListener extends LDrawParserBaseListener
{ 	
	private Model assetModel;
	private Resource asset;
	private Resource distribution;
	
	private String fileName;
	private String basename;
	
	private IGraphStore graphStore;
	
	
	public AssetListener(String fileName, IGraphStore graphStore)
	{
		super();
		
		this.fileName  = fileName;
		this.basename = FilenameUtils.getBaseName(fileName);
		this.graphStore = graphStore;
	}


	@Override
	public void enterFile(FileContext ctx) 
	{
		// set NS prefixes
		assetModel = ModelFactory.createDefaultModel();
		assetModel.setNsPrefixes(FOAF.NAMESPACE);
		assetModel.setNsPrefixes(ADMS.NAMESPACE);
		assetModel.setNsPrefixes(DCAT.NAMESPACE);
		assetModel.setNsPrefix("dcterms", DCTerms.NS);
		assetModel.setNsPrefix("rdf", RDF.getURI());
		assetModel.setNsPrefix("rdfs", RDFS.getURI());
		assetModel.setNsPrefix("skos", SKOS.getURI());
		assetModel.setNsPrefix("xsd", XSD.NS);
		assetModel.setNsPrefix("ldraw", "http://www.ldraw.org/ns/ldraw#");
		assetModel.setNsPrefix("users", App.fUserBaseUri);
		assetModel.setNsPrefix("assets", App.fAssetBaseUri);
		assetModel.setNsPrefix("distributions", App.fDistributionBaseUri);

		// create asset resource
		asset = assetModel.createResource(Util.joinPath(App.fAssetBaseUri, Util.urlEncoded(basename)));
		assetModel.add( asset, RDF.type, ADMS.Asset );

//		landingPage = assetModel.createResource(asset.getURI() + ".html" );
//		assetModel.add(landingPage, RDF.type, FOAF.Document);
//		assetModel.add( asset, DCAT.landingPage, landingPage);
		
		// create and add distribution resource 
		distribution = assetModel.createResource(Util.joinPath(App.fDistributionBaseUri, Util.urlEncoded(basename)));
		assetModel.add( distribution, RDF.type, ADMS.AssetDistribution );
		assetModel.add( distribution, DCTerms.format, "application/x-ldraw" );
		assetModel.add( distribution, DCAT.mediaType, "application/x-ldraw" );
		Literal downloadURL = assetModel.createTypedLiteral( Util.joinPath(App.fStorageURI, Util.urlEncoded(fileName)), XSDDatatype.XSDanyURI );
		assetModel.add( distribution, DCAT.downloadURL, downloadURL);
		assetModel.add( asset, DCAT.distribution, distribution );
		
		Resource repo = assetModel.createResource(Util.joinPath(App.fBaseURI, "repo"));
		assetModel.add( repo, RDF.type, ADMS.AssetRepository);
		assetModel.add( asset, DCTerms.isReferencedBy, repo );
	};
	
	@Override
	public void exitFile(FileContext ctx) 
	{
		graphStore.addToNamedGraph(asset.getURI(), assetModel);
		
		// slow down to prevent org.apache.jena.atlas.web.HttpException: 500 - Direct buffer memory
		try 
		{
			Thread.sleep(50);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	};
	
	
	@Override
	public void exitTitle(TitleContext ctx)
	{
		if (ctx.free_text() != null )
			assetModel.add( asset, DCTerms.description, Util.toStringLiteral(ctx.free_text(), " ") );
	}

	@Override
	public void exitAuthor_row(Author_rowContext ctx)
	{
		if (ctx != null)
		{
			if (ctx.realname() != null)
			{
				Resource creator = assetModel.createResource(Util.joinPath(App.fUserBaseUri, Util.toURLEncodedStringLiteral(ctx.realname().STRING(), "_").getString()));
				assetModel.add( asset, FOAF.maker, creator );
				assetModel.add( asset,  DCTerms.creator, creator );
				
//				assetModel.add( creator, RDF.type, FOAF.Agent );
//				assetModel.add( creator, FOAF.name, Util.toStringLiteral(ctx.realname().STRING(), " ") );
//				assetModel.add( creator, FOAF.made, asset);
//
//				
//
//				if (ctx.username() != null)
//				{
//					Resource account = assetModel.createResource();
//					assetModel.add( account, RDF.type, FOAF.OnlineAccount );
//					assetModel.add( account, FOAF.accountServiceHomepage, assetModel.createTypedLiteral("http://ldraw.org/", XSDDatatype.XSDanyURI) );
//					assetModel.add( account, FOAF.accountName, ctx.username().STRING().getText() );
//					assetModel.add( creator, FOAF.account, account );
//				}
			}
		}
	}
	
	@Override
	public void exitCategory_row(Category_rowContext ctx)
	{	
		if (ctx.category() != null)
			for (TerminalNode c : ctx.category().STRING())
				assetModel.add( asset, DCAT.theme, c.getText() );
	}

	@Override
	public void exitComment_row(Comment_rowContext ctx)
	{
		if (ctx.free_text() != null)
			assetModel.add( asset, RDFS.comment, Util.toStringLiteral(ctx.free_text(), " ") );
	}

	@Override
	public void exitHistory_row(History_rowContext ctx)
	{
		if (ctx != null)
		{
			Resource changeNote = assetModel.createResource();
			assetModel.add( changeNote, DCTerms.date, assetModel.createTypedLiteral(ctx.YYYY_MM_DD().getText(), XSDDatatype.XSDdate));
			assetModel.add( changeNote, RDF.value, Util.toStringLiteral(ctx.free_text(), " ") );
			assetModel.add( asset, SKOS.changeNote, changeNote );
			
			if (ctx.realname() != null)
			{
				Resource contributor = assetModel.createResource(Util.joinPath(App.fUserBaseUri, Util.toURLEncodedStringLiteral(ctx.realname().STRING(), "_").toString()));
				assetModel.add( changeNote,  DCTerms.creator, contributor);
				assetModel.add( contributor, DCTerms.contributor, asset );
				
//				assetModel.add( contributor, RDF.type, FOAF.Agent );
//				assetModel.add( contributor, FOAF.name, Util.toStringLiteral(ctx.realname().STRING(), " ") );

			}

			// TODO: ctx.username()
		}
	}
	
	@Override
	public void exitKeywords_row(Keywords_rowContext ctx)
	{
		if (ctx.free_text() != null)
			assetModel.add( asset, DCAT.keyword, Util.toStringLiteral(ctx.free_text(), " ") );
	}

	@Override
	public void exitLicense_row(License_rowContext ctx)
	{
		if (ctx.free_text() != null)
		{
			Resource rightsStatement = assetModel.createResource();
			assetModel.add( rightsStatement, RDF.type, DCTerms.RightsStatement );
			assetModel.add( rightsStatement, RDFS.label, Util.toStringLiteral(ctx.free_text(), " ") );
			assetModel.add( asset, DCTerms.rights, rightsStatement );
		}
	}

//	@Override
//	public void exitName_row(Name_rowContext ctx)
//	{			  
//		if (ctx != null)
//		{
//			Resource distribution = assetModel.createResource(Util.joinPath(App.fBaseURI, "repo/distributions/") + Util.urlEncoded(ctx.FILENAME().getText()));
//			assetModel.add( asset, DCAT.distribution, distribution );
//		}
//	}

	@Override
	public void exitReference_row(Reference_rowContext ctx)
	{
		if (ctx.subPart() != null)
		{
			Resource subPart = assetModel.createResource(Util.joinPath(App.fAssetBaseUri, Util.urlEncoded(FilenameUtils.getBaseName(ctx.subPart().FILENAME().getText())).toString()));
			assetModel.add( asset, ADMS.includedAsset, subPart );
		}
		if (ctx.subFile() != null)
		{
			Resource subFile = assetModel.createResource(Util.joinPath(App.fAssetBaseUri, Util.urlEncoded(FilenameUtils.getBaseName(ctx.subFile().FILENAME().getText())).toString()));
			assetModel.add( asset, ADMS.includedAsset, subFile );
		}
		if (ctx.hiResPrimitive() != null)
		{
			Resource hisResPrimitive = assetModel.createResource(Util.joinPath(App.fAssetBaseUri, Util.urlEncoded(FilenameUtils.getBaseName(ctx.hiResPrimitive().FILENAME().getText())).toString()));
			assetModel.add( asset, ADMS.includedAsset, hisResPrimitive );
		}
	}

	public void exitHelp_row(Help_rowContext ctx)
	{
		if (ctx.free_text() != null)
		{
			assetModel.add( asset, SKOS.note, Util.toStringLiteral(ctx.free_text(), " ") );
		}
	}

	public void exitLdraw_row(Ldraw_rowContext ctx)
	{
		if (ctx != null)
		{
			assetModel.add( asset, DCTerms.type, assetModel.createResource("http://www.ldraw.org/ns/ldraw#" + Util.urlEncoded(ctx.type().TYPE().getText())));
//			String downloadURL ;
//			
//			if (ctx.type().TYPE().getText().contains("Unofficial_Part") | ctx.type().TYPE().getText().contains("Unofficial_Subpart") | ctx.type().TYPE().getText().contains("Unofficial_Sub-part"))
//			{
//				downloadURL = Util.appendSegmentToPath("http://www.ldraw.org/library/unofficial/parts/", Util.urlEncoded(basename));
//				assetModel.add( distribution, DCAT.downloadURL, assetModel.createTypedLiteral(downloadURL, XSDDatatype.XSDanyURI));
//			}
//			else if (ctx.type().TYPE().getText().contains("Part") | ctx.type().TYPE().getText().contains("Subpart") | ctx.type().TYPE().getText().contains("Sub-part"))
//			{
//				downloadURL = Util.appendSegmentToPath("http://www.ldraw.org/library/unofficial/parts/", Util.urlEncoded(basename));
//				assetModel.add( distribution, DCAT.downloadURL, assetModel.createTypedLiteral(downloadURL, XSDDatatype.XSDanyURI));
//			}
		}
	}
}
