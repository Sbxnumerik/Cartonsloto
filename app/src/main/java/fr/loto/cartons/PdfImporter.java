package fr.loto.cartons;
import android.content.Context;
import android.net.Uri;
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import java.io.*;import java.util.*;import java.util.regex.*;
public final class PdfImporter {
 private static final Pattern ID=Pattern.compile("\\b(\\d{2}-\\d{4})\\b");
 public static ArrayList<Game.Card> read(Context ctx,Uri uri)throws Exception{
  PDFBoxResourceLoader.init(ctx);ArrayList<Game.Card> result=new ArrayList<>();
  try(InputStream in=ctx.getContentResolver().openInputStream(uri);PDDocument pdf=PDDocument.load(in)){
   PDFTextStripper stripper=new PDFTextStripper();stripper.setSortByPosition(false);
   for(int page=1;page<=pdf.getNumberOfPages();page++){
    stripper.setStartPage(page);stripper.setEndPage(page);String txt=stripper.getText(pdf);
    String[] sections=txt.split("(?i)LOTOQUINE");
    for(int i=1;i<sections.length;i++){
     Matcher id=ID.matcher(sections[i]);if(!id.find())continue;
     String body=sections[i].substring(0,id.start());Matcher numbers=Pattern.compile("(?<!\\d)\\d{1,2}(?!\\d)").matcher(body);
     ArrayList<Integer> values=new ArrayList<>();while(numbers.find())values.add(Integer.parseInt(numbers.group()));
     if(values.size()!=15)throw new IOException("Lecture incertaine du carton "+id.group()+" : "+values.size()+" numéros. Import annulé.");
     Game.Card card=new Game.Card(id.group());for(int j=0;j<15;j++){int n=values.get(j);int col=n==90?8:n/10;card.grid[j/5][col]=n;}
     new Game().validate(card);result.add(card);
    }
   }
  }
  if(result.isEmpty())throw new IOException("Aucun carton reconnu. PDF texte LOTOQUINE requis.");return result;
 }
}