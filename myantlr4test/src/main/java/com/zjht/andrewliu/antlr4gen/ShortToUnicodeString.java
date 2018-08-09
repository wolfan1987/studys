package com.zjht.andrewliu.antlr4gen;

/**
 * Created by liudaan on 2016/10/21.
 */
public class ShortToUnicodeString extends  ArrayInitBaseListener {

    @Override
    public void enterInit(ArrayInitParser.InitContext ctx) {
        System.out.println("enterInit..........");
    }


    @Override
    public void exitInit(ArrayInitParser.InitContext ctx) {
        System.out.println("");
    }

    @Override
    public void enterValue(ArrayInitParser.ValueContext ctx) {
        if(ctx.INT() == null){
            System.out.println(ctx.INT());
        }else{
            System.out.printf("\\u%04x",Integer.valueOf(ctx.INT().getText()));
        }
    }
}
