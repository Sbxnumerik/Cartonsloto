package fr.loto.cartons;
import android.graphics.Bitmap;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import java.util.ArrayList;
public final class CardOcr {
 private CardOcr(){}
 public interface Callback { void done(Game.Card card); void error(Exception e); }
 public static void read(Bitmap photo,Callback callback){
  com.google.mlkit.vision.text.TextRecognizer recognizer=TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
  recognizer.process(InputImage.fromBitmap(photo,0)).addOnSuccessListener(result->{
   Game.Card card=new Game.Card("");
   ArrayList<Text.Element> digits=new ArrayList<>();
   for(Text.TextBlock block:result.getTextBlocks())for(Text.Line line:block.getLines())for(Text.Element element:line.getElements()){
    String t=element.getText().replaceAll("[^0-9]","");
    if(t.matches("[0-9]{1,2}"))digits.add(element);
   }
   if(!digits.isEmpty()){
    int left=Integer.MAX_VALUE,right=Integer.MIN_VALUE,top=Integer.MAX_VALUE,bottom=Integer.MIN_VALUE;
    for(Text.Element e:digits){android.graphics.Rect r=e.getBoundingBox();if(r==null)continue;left=Math.min(left,r.centerX());right=Math.max(right,r.centerX());top=Math.min(top,r.centerY());bottom=Math.max(bottom,r.centerY());}
    if(right>left&&bottom>top){
     for(Text.Element e:digits){android.graphics.Rect r=e.getBoundingBox();if(r==null)continue;try{
      int n=Integer.parseInt(e.getText().replaceAll("[^0-9]",""));if(n<1||n>90)continue;
      int col=Math.min(8,Math.max(0,Math.round((r.centerX()-left)*8f/(right-left))));
      int row=Math.min(2,Math.max(0,Math.round((r.centerY()-top)*2f/(bottom-top))));
      if(card.grid[row][col]==0)card.grid[row][col]=n;
     }catch(Exception ignored){}}
    }
   }
   recognizer.close();callback.done(card);
  }).addOnFailureListener(e->{recognizer.close();callback.error(e);});
 }
}
