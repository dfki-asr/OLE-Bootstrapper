/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.resc28.ole.bootstrap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang.StringUtils;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 *
 * @author Dmitri Rubinstein
 */
public final class Util {

    public static String urlEncoded(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            System.err.format("Could not convert string to UTF-8: %s%n", ex);
            return text;
        }
    }

    public static Literal toURLEncodedStringLiteral(List<TerminalNode> tokens, String separator) {
        return ResourceFactory.createTypedLiteral(Util.urlEncoded(StringUtils.join(tokens.toArray(), separator)), XSDDatatype.XSDstring);
    }

    public static Literal toStringLiteral(List<TerminalNode> tokens, String separator) {
        return ResourceFactory.createTypedLiteral(StringUtils.join(tokens.toArray(), separator), XSDDatatype.XSDstring);
    }

    public static Literal toStringLiteral(RuleContext ctx, String separator) {
        if (ctx != null) {
            String descr[] = new String[ctx.getPayload().getChildCount()];
            for (int i = 0; i < ctx.getPayload().getChildCount(); i++) {
                descr[i] = ctx.getPayload().getChild(i).getText();
            }

            return ResourceFactory.createTypedLiteral(StringUtils.join(descr, separator), XSDDatatype.XSDstring);
        } else {
            return ResourceFactory.createPlainLiteral("Something went wrong!");
        }
    }

    public static String appendSegmentToPath(String path, String segment) {
        boolean segmentStartsWithSlash = !segment.isEmpty() && segment.charAt(0) == '/';

        if (path == null || path.isEmpty()) {
            return segmentStartsWithSlash ? segment : "/" + segment;
        }

        if (path.charAt(path.length() - 1) == '/') {
            return segmentStartsWithSlash ? path + segment.substring(1) : path + segment;
        }

        return segmentStartsWithSlash ? path + segment : path + "/" + segment;
    }

    public static String joinPath(String... args) {
        if (args.length == 0)
            return "";
        String path = args[0];
        for (int i = 1; i < args.length; ++i) {
            path = appendSegmentToPath(path, args[i]);
        }
        return path;
    }

}
