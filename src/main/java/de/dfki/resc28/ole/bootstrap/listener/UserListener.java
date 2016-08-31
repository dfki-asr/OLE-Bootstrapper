/*
 * This file is part of OLE. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */

package de.dfki.resc28.ole.bootstrap.listener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;
import org.apache.jena.vocabulary.XSD;

import de.dfki.resc28.LDrawParser.LDrawParser.Author_rowContext;
import de.dfki.resc28.LDrawParser.LDrawParser.FileContext;
import de.dfki.resc28.LDrawParser.LDrawParser.History_rowContext;
import de.dfki.resc28.LDrawParser.LDrawParserBaseListener;
import de.dfki.resc28.igraphstore.IGraphStore;
import de.dfki.resc28.ole.bootstrap.App;
import de.dfki.resc28.ole.bootstrap.vocabularies.ADMS;
import de.dfki.resc28.ole.bootstrap.vocabularies.DCAT;
import de.dfki.resc28.ole.bootstrap.vocabularies.FOAF;

public class UserListener extends LDrawParserBaseListener
{
	private Resource asset;
	private Model userModel;
	private Resource user;
	private String userBaseUri = App.fBaseURI + "repo/users/" ;
	
	private String basename;
	private String extension;
	private IGraphStore graphStore;
	
	public UserListener(String fileName, IGraphStore graphStore)
	{
		super();
		
		this.basename = FilenameUtils.getBaseName(fileName);
		this.extension = FilenameUtils.getExtension(fileName);
		this.graphStore = graphStore;
	}
	
	@Override
	public void enterFile(FileContext ctx) 
	{
		userModel = ModelFactory.createDefaultModel();
		userModel.setNsPrefixes(FOAF.NAMESPACE);
		userModel.setNsPrefixes(ADMS.NAMESPACE);
		userModel.setNsPrefixes(DCAT.NAMESPACE);
		userModel.setNsPrefix("dcterms", DCTerms.NS);
		userModel.setNsPrefix("rdf", RDF.getURI());
		userModel.setNsPrefix("rdfs", RDFS.getURI());
		userModel.setNsPrefix("skos", SKOS.getURI());
		userModel.setNsPrefix("xsd", XSD.NS);
		userModel.setNsPrefix("ldraw", "http://www.ldraw.org/ns/ldraw#");
		userModel.setNsPrefix("users", App.fBaseURI + "repo/users/");
		userModel.setNsPrefix("assets", App.fBaseURI + "repo/assets/");
		userModel.setNsPrefix("distributions", App.fBaseURI + "repo/distributions/");
		
		asset = userModel.createResource(App.fBaseURI + "repo/assets/" + basename);
	};
	
	@Override
	public void exitFile(FileContext ctx) 
	{
		if (user != null)
		{
			graphStore.addToNamedGraph(user.getURI(), userModel);
			
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
				Resource user = ResourceFactory.createResource(userBaseUri + toStringLiteral(ctx.realname().STRING(), "_"));
				
				// only add user if not known yet!
				if (!graphStore.containsNamedGraph(user.getURI()))
				{

					userModel.add( user, RDF.type, FOAF.Agent );		
					userModel.add( user, FOAF.name, toStringLiteral(ctx.realname().STRING(), " ") );
					userModel.add( user, FOAF.made, asset);				
					
					if (ctx.username() != null)
					{
						Resource account = userModel.createResource();
						userModel.add( account, RDF.type, FOAF.OnlineAccount );
						userModel.add( account, FOAF.accountServiceHomepage, userModel.createTypedLiteral("http://ldraw.org/", XSDDatatype.XSDanyURI) );
						userModel.add( account, FOAF.accountName, ctx.username().STRING().getText() );
						userModel.add( user, FOAF.account, account );
					}				
				}
			}
		}
	}

	
	@Override
	public void exitHistory_row(History_rowContext ctx)
	{
		if (ctx != null)
		{
			if (ctx.realname() != null)
			{
				Resource creator = ResourceFactory.createResource(userBaseUri + toStringLiteral(ctx.realname().STRING(), "_"));
				
				// only add user if not known yet!
				if (!graphStore.containsNamedGraph(creator.getURI()))
				{

					userModel.add( creator, RDF.type, FOAF.Agent );
					userModel.add( creator, FOAF.name, toStringLiteral(ctx.realname().STRING(), " ") );
				}
			}
			
			if (ctx.username() != null)
			{
				// TODO: // check if we know a user with ctx.username() else create this user
			}
		}
	}
	
	
	
	
	
	
	
	private String toPlainString(RuleContext ctx, String separator) 
	{
		if (ctx != null)
		{
			String descr[] = new String[ctx.getPayload().getChildCount()];
			for (int i = 0; i<ctx.getPayload().getChildCount(); i++)
				descr[i] = ctx.getPayload().getChild(i).getText();
	
			return StringUtils.join(descr, separator);
		}
		else
		{
			return "Something went wrong!";
		}
	}

	private Literal toStringLiteral(RuleContext ctx, String separator) 
	{
		if (ctx != null)
		{
			String descr[] = new String[ctx.getPayload().getChildCount()];
			for (int i = 0; i<ctx.getPayload().getChildCount(); i++)
				descr[i] = ctx.getPayload().getChild(i).getText();
	
			return ResourceFactory.createTypedLiteral(StringUtils.join(descr, separator), XSDDatatype.XSDstring);
		}
		else
		{
			return ResourceFactory.createPlainLiteral("Something went wrong!");
		}
	}


	
	private Literal toStringLiteral(List<TerminalNode> tokens, String separator)
	{
		return ResourceFactory.createTypedLiteral(StringUtils.join(tokens.toArray(), separator), XSDDatatype.XSDstring);
	}
}
