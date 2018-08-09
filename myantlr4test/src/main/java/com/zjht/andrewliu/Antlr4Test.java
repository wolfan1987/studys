package com.zjht.andrewliu;

import com.zjht.andrewliu.antlr4gen.ArrayInitLexer;
import com.zjht.andrewliu.antlr4gen.ArrayInitParser;
import com.zjht.andrewliu.antlr4gen.ShortToUnicodeString;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * Created by liudaan on 2016/10/20.
 */
public class Antlr4Test {


    public static void main(String[] args) {
        String sentence = "{100,3,451}";
        ArrayInitLexer lexer = new ArrayInitLexer(new ANTLRInputStream(sentence));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ArrayInitParser parser = new ArrayInitParser(tokens);
        ParseTree tree = parser.init();

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new ShortToUnicodeString(), tree);
        System.out.println("end.........");
    }
}
