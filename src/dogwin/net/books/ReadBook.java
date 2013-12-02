package dogwin.net.books;import java.io.BufferedReader;import java.io.File;import java.io.FileReader;import java.io.IOException;import android.app.Activity;import android.content.Intent;import android.os.Bundle;import android.os.Environment;import android.text.method.ScrollingMovementMethod;import android.widget.TextView;import dogwin.net.apps.R;import dogwin.net.publics.JSONParser;public class ReadBook extends Activity{	String folder;	String filename;	JSONParser jsonParser = new JSONParser();	String readbook;	TextView bookcontent;		@Override	protected void onCreate(Bundle savedInstanceState) {		// TODO Auto-generated method stub		Intent intent = getIntent();		folder = intent.getStringExtra("folder");		filename = intent.getStringExtra("filename");		super.onCreate(savedInstanceState);		setContentView(R.layout.readbook);				File filepath = new File(Environment.getExternalStorageDirectory().toString()+"/"+folder+"/"+filename);		readBook(filepath);		//System.out.println(readBook(bookName));	}	private void readBook(File file){		StringBuilder text = new StringBuilder();		try {		    BufferedReader br = new BufferedReader(new FileReader(file));		    String line;		    while ((line = br.readLine()) != null) {		        text.append(line);		        text.append('\n');		    }		}		catch (IOException e) {		    //You'll need to add proper error handling here		}		//Find the view by its id		bookcontent = (TextView)this.findViewById(R.id.bookcontent);		//Set the text		bookcontent.setMovementMethod(new ScrollingMovementMethod());		bookcontent.setText(text);	}}