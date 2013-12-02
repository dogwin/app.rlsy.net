package dogwin.net.books;import java.io.File;import java.io.FilenameFilter;import java.io.InputStream;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import org.json.JSONArray;import org.json.JSONException;import org.json.JSONObject;import android.annotation.SuppressLint;import android.annotation.TargetApi;import android.app.Activity;import android.app.Notification;import android.app.NotificationManager;import android.app.PendingIntent;import android.app.ProgressDialog;import android.content.Context;import android.content.DialogInterface;import android.content.Intent;import android.content.IntentFilter;import android.content.SharedPreferences;import android.net.ConnectivityManager;import android.os.Build;import android.os.Bundle;import android.os.Environment;import android.preference.EditTextPreference;import android.util.Log;import android.view.Menu;import android.view.MenuInflater;import android.view.MenuItem;import android.view.View;import android.widget.ExpandableListView;import android.widget.ExpandableListView.OnChildClickListener;import android.widget.ExpandableListView.OnGroupCollapseListener;import android.widget.ExpandableListView.OnGroupExpandListener;import android.widget.Toast;import dogwin.net.apps.R;import dogwin.net.check.DwClient;import dogwin.net.publics.DownloadTask;import dogwin.net.publics.FileISO;import dogwin.net.publics.JSONParser;import dogwin.net.publics.Menus;public class BooksApp extends Activity{		int pid,bookid;	public boolean IconFlag=true;	Intent bintent;	String typeName,booktypeurl,booklisturl;	//check sharepreferences	public EditTextPreference editText;	public SharedPreferences preferences;	static String uc_username,uc_password;	static boolean uc_flag;	Menus menus = new Menus();	JSONParser jsonParser = new JSONParser();	FileISO fileISO = new FileISO();	ExpandableListAdapter listAdapter;    ExpandableListView expListView;    List<String> listDataHeader;    HashMap<String, List<String>> listDataChild;    Context context;	@Override	protected void onCreate(Bundle savedInstanceState) {		// TODO Auto-generated method stub		super.onCreate(savedInstanceState);		setContentView(R.layout.booklist);		this.pid = android.os.Process.myPid();		uc_flag = Rt_flag();		IntentFilter mFilter = new IntentFilter();          mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);          File SDCardRoot = new File(Environment.getExternalStorageDirectory()                .toString() + "/Rlsy/books");        fileISO.writeFolder(SDCardRoot);        // get the listview        expListView = (ExpandableListView) findViewById(R.id.lvExp);        // preparing list data        prepareListData();         listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);         // setting list adapter        expListView.setAdapter(listAdapter);         // Listview Group click listener        /*expListView.setOnGroupClickListener(new OnGroupClickListener() { 			            @Override            public boolean onGroupClick(ExpandableListView parent, View v,                    int groupPosition, long id) {                 Toast.makeText(getApplicationContext(),                 "Group Clicked " + listDataHeader.get(groupPosition),                 Toast.LENGTH_SHORT).show();                return false;            }        });        */        // Listview Group expanded listener        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {             @Override            public void onGroupExpand(int groupPosition) {                Toast.makeText(getApplicationContext(),                        listDataHeader.get(groupPosition) + " Expanded",                        Toast.LENGTH_SHORT).show();                            }        });         // Listview Group collasped listener        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {             @Override            public void onGroupCollapse(int groupPosition) {                Toast.makeText(getApplicationContext(),                        listDataHeader.get(groupPosition) + " Collapsed",                        Toast.LENGTH_SHORT).show();             }        });         // Listview on child click listener        expListView.setOnChildClickListener(new OnChildClickListener() {             @Override            public boolean onChildClick(ExpandableListView parent, View v,                    int groupPosition, int childPosition, long id) {                // TODO Auto-generated method stub                /*Toast.makeText(                        getApplicationContext(),                        listDataHeader.get(groupPosition)                                + " : "                                + listDataChild.get(                                        listDataHeader.get(groupPosition)).get(                                        childPosition), Toast.LENGTH_SHORT)                        .show();*/            	//readBook(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition),listDataHeader.get(groupPosition));            	String[] obj = readBook(listDataChild.get(                        listDataHeader.get(groupPosition)).get(                        childPosition));            	System.out.println(obj);            	            	File folder = new File(Environment.getExternalStorageDirectory()                        .toString()+"/"+obj[2]);            	File fileName = new File(Environment.getExternalStorageDirectory()                        .toString()+"/"+obj[2]+"/"+obj[3]);            	//String fileName = obj[3];            	            	fileISO.writeFolder(folder);            	//String bookName = folder+"/"+fileName;            	/**            	 * txt,pdf,folder,fileName            	 */            	String[] urls = {            			obj[0],obj[2],obj[3]            	};            	if(fileISO.bookExist(fileName)){            		System.out.println("found file"+fileName);            	}else{            		download(urls);            	}            	                return false;            }        });	}		/*     * Preparing the list data     */    private void prepareListData() {        listDataHeader = new ArrayList<String>();        listDataChild = new HashMap<String, List<String>>();        booktypeurl = getResources().getString(R.string.booktypeurl);        JSONObject json = jsonParser.getJSONFromUrl(booktypeurl);        try {			JSONArray contacts = json.getJSONArray("content");			for(int i = 0; i < contacts.length(); i++){				JSONObject c = contacts.getJSONObject(i);				String typeName = c.getString("typeName");				int typeID = c.getInt("id");				listDataHeader.add(typeName);				/*File SDCardRoot = new File(Environment.getExternalStorageDirectory()		                .toString() + "/Rlsy/books/"+typeName);				fileISO.writeFolder(SDCardRoot);*/								// Adding child data				//bookName(typeID,i);				List<String> top = new ArrayList<String>();				//top = bookName(typeID);				JSONArray booklistArr = c.getJSONArray("booklist");				//System.out.println(booklistArr);				for(int j = 0; j < booklistArr.length(); j++){					JSONObject obj = booklistArr.getJSONObject(j);					String bookName = obj.getString("bookName");					int bookID = obj.getInt("id");					top.add(bookName);				}				listDataChild.put(listDataHeader.get(i), top);			}					} catch (JSONException e) {			// TODO Auto-generated catch block			e.printStackTrace();		}    }	//    //read book    private String[] readBook(String bookName){    			String txt,pdf,folder,fileName;		String readbook = getResources().getString(R.string.readbookurl)+"/"+bookName;		JSONObject json = jsonParser.getJSONFromUrl(readbook);		//return readbook;		try {			JSONObject contacts = json.getJSONObject("content");			System.out.println();			//JSONObject c = contacts.getJSONObject(0);			txt = contacts.getString("txt");			pdf = contacts.getString("pdf");			folder = contacts.getString("folder");			fileName = contacts.getString("filename");			String[] obj = {				txt,pdf,folder,fileName			};			return obj;		} catch (JSONException e) {			// TODO Auto-generated catch block			e.printStackTrace();			return null;		}			}        	private static String convertStreamToString(InputStream instream) {		// TODO Auto-generated method stub		return null;	}	@Override	public boolean onCreateOptionsMenu(Menu menu) {		MenuInflater inflater = new MenuInflater(this);		if(DwClient.flag||uc_flag){			inflater.inflate(R.menu.menu_option_main, menu);		}else{			inflater.inflate(R.menu.menu_unlogin, menu);		}		return super.onCreateOptionsMenu(menu);			}	@Override	public boolean onOptionsItemSelected(MenuItem item) {		boolean menu_flag = false;		IconFlag = false;		if(DwClient.flag||uc_flag){			menu_flag = true;		}		return super.onOptionsItemSelected(menus.select_menus(item, BooksApp.this, pid, menu_flag));			}		@Override	protected void onStop() {		// TODO Auto-generated method stub		super.onStop();		Log.v("TAG", "back Run");		if(IconFlag){			createNotification();		}	}	@Override	protected void onRestart() {		// TODO Auto-generated method stub		super.onRestart();		IconFlag = false;	}				//Icon Notification		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)	@SuppressLint("NewApi")	public void createNotification(){		Intent intent = new Intent(this, BooksApp.class);		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);		//title		String title = getResources().getString(R.string.app_name);		String subject = getResources().getString(R.string.subject);				Notification noti = new Notification.Builder(this)        .setContentTitle(title)        .setContentText(subject).setSmallIcon(R.drawable.ic_launcher)        .setContentIntent(pIntent).build();		 NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);		    // Hide the notification after its selected		 noti.flags |= Notification.FLAG_AUTO_CANCEL;		 notificationManager.notify(0, noti);	}	//get user current	public String Rt_username(){		preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);	    return preferences.getString("username", null);	}			public String Rt_password(){		preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);	    return preferences.getString("password", null);	}		public boolean Rt_flag(){		preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);		return preferences.getBoolean("flag", false);	}	public void download(String... url){		// declare the dialog as a member field of your activity		ProgressDialog mProgressDialog;		// instantiate it within the onCreate method		mProgressDialog = new ProgressDialog(BooksApp.this);		mProgressDialog.setMessage("A message");		mProgressDialog.setIndeterminate(true);		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);		mProgressDialog.setCancelable(true);		// execute this when the downloader must be fired		final DownloadTask downloadTask = new DownloadTask(BooksApp.this);		downloadTask.execute(url);				mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {		    @Override		    public void onCancel(DialogInterface dialog) {		        downloadTask.cancel(true);		    }		});	}}