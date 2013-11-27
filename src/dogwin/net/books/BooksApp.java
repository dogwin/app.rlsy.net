package dogwin.net.books;import java.io.InputStream;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import android.annotation.SuppressLint;import android.annotation.TargetApi;import android.app.Activity;import android.app.Notification;import android.app.NotificationManager;import android.app.PendingIntent;import android.content.BroadcastReceiver;import android.content.Context;import android.content.Intent;import android.content.IntentFilter;import android.content.SharedPreferences;import android.net.ConnectivityManager;import android.os.Build;import android.os.Bundle;import android.preference.EditTextPreference;import android.util.Log;import android.view.Menu;import android.view.MenuInflater;import android.view.MenuItem;import android.view.View;import android.widget.ExpandableListView;import android.widget.ExpandableListView.OnChildClickListener;import android.widget.ExpandableListView.OnGroupCollapseListener;import android.widget.ExpandableListView.OnGroupExpandListener;import android.widget.Toast;import dogwin.net.apps.R;import dogwin.net.check.Connectivity;import dogwin.net.check.DwClient;import dogwin.net.publics.JSONParser;import dogwin.net.publics.Menus;public class BooksApp extends Activity{		int pid,bookid;	public boolean IconFlag=true;	Intent bintent;	String typeName,booktypeurl;	//check sharepreferences	public EditTextPreference editText;	public SharedPreferences preferences;	static String uc_username,uc_password;	static boolean uc_flag;	Menus menus = new Menus();	JSONParser jsonParser = new JSONParser();	ExpandableListAdapter listAdapter;    ExpandableListView expListView;    List<String> listDataHeader;    HashMap<String, List<String>> listDataChild;    Context context;	@Override	protected void onCreate(Bundle savedInstanceState) {		// TODO Auto-generated method stub		super.onCreate(savedInstanceState);		setContentView(R.layout.booklist);		this.pid = android.os.Process.myPid();		uc_flag = Rt_flag();		IntentFilter mFilter = new IntentFilter();          mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);               //registerReceiver(mReceiver, mFilter);        //booktypeurl = getResources().getString(R.string.booktypeurl);                // get the listview        expListView = (ExpandableListView) findViewById(R.id.lvExp);        // preparing list data        prepareListData();         listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);         // setting list adapter        expListView.setAdapter(listAdapter);         // Listview Group click listener        /*expListView.setOnGroupClickListener(new OnGroupClickListener() { 			            @Override            public boolean onGroupClick(ExpandableListView parent, View v,                    int groupPosition, long id) {                // Toast.makeText(getApplicationContext(),                // "Group Clicked " + listDataHeader.get(groupPosition),                // Toast.LENGTH_SHORT).show();                return false;            }        });*/                // Listview Group expanded listener        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {             @Override            public void onGroupExpand(int groupPosition) {                Toast.makeText(getApplicationContext(),                        listDataHeader.get(groupPosition) + " Expanded",                        Toast.LENGTH_SHORT).show();            }        });         // Listview Group collasped listener        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {             @Override            public void onGroupCollapse(int groupPosition) {                Toast.makeText(getApplicationContext(),                        listDataHeader.get(groupPosition) + " Collapsed",                        Toast.LENGTH_SHORT).show();             }        });         // Listview on child click listener        expListView.setOnChildClickListener(new OnChildClickListener() {             @Override            public boolean onChildClick(ExpandableListView parent, View v,                    int groupPosition, int childPosition, long id) {                // TODO Auto-generated method stub                Toast.makeText(                        getApplicationContext(),                        listDataHeader.get(groupPosition)                                + " : "                                + listDataChild.get(                                        listDataHeader.get(groupPosition)).get(                                        childPosition), Toast.LENGTH_SHORT)                        .show();                return false;            }        });	}		/*     * Preparing the list data     */    private void prepareListData() {        listDataHeader = new ArrayList<String>();        listDataChild = new HashMap<String, List<String>>();                       // Adding child data        listDataHeader.add("Top 250");        listDataHeader.add("Now Showing");        listDataHeader.add("Coming Soon..");                // Adding child data        List<String> top = new ArrayList<String>();        top.add("The Shawshank Redemption");        top.add("The Godfather");        top.add("The Godfather: Part II");        top.add("Pulp Fiction");        top.add("The Good, the Bad and the Ugly");        top.add("The Dark Knight");        top.add("12 Angry Men");         List<String> nowShowing = new ArrayList<String>();        nowShowing.add("The Conjuring");        nowShowing.add("Despicable Me 2");        nowShowing.add("Turbo");        nowShowing.add("Grown Ups 2");        nowShowing.add("Red 2");        nowShowing.add("The Wolverine");         List<String> comingSoon = new ArrayList<String>();        comingSoon.add("2 Guns");        comingSoon.add("The Smurfs 2");        comingSoon.add("The Spectacular Now");        comingSoon.add("The Canyons");        comingSoon.add("Europa Report");         listDataChild.put(listDataHeader.get(0), top); // Header, Child data        listDataChild.put(listDataHeader.get(1), nowShowing);        listDataChild.put(listDataHeader.get(2), comingSoon);    }		private static String convertStreamToString(InputStream instream) {		// TODO Auto-generated method stub		return null;	}	@Override	public boolean onCreateOptionsMenu(Menu menu) {		MenuInflater inflater = new MenuInflater(this);		if(DwClient.flag||uc_flag){			inflater.inflate(R.menu.menu_option_main, menu);		}else{			inflater.inflate(R.menu.menu_unlogin, menu);		}		return super.onCreateOptionsMenu(menu);			}	@Override	public boolean onOptionsItemSelected(MenuItem item) {		boolean menu_flag = false;		IconFlag = false;		if(DwClient.flag||uc_flag){			menu_flag = true;		}		return super.onOptionsItemSelected(menus.select_menus(item, BooksApp.this, pid, menu_flag));			}		@Override	protected void onStop() {		// TODO Auto-generated method stub		super.onStop();		Log.v("TAG", "back Run");		if(IconFlag){			createNotification();		}	}	@Override	protected void onRestart() {		// TODO Auto-generated method stub		super.onRestart();		IconFlag = false;	}				//Icon Notification		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)	@SuppressLint("NewApi")	public void createNotification(){		Intent intent = new Intent(this, BooksApp.class);		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);		//title		String title = getResources().getString(R.string.app_name);		String subject = getResources().getString(R.string.subject);				Notification noti = new Notification.Builder(this)        .setContentTitle(title)        .setContentText(subject).setSmallIcon(R.drawable.ic_launcher)        .setContentIntent(pIntent).build();		 NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);		    // Hide the notification after its selected		 noti.flags |= Notification.FLAG_AUTO_CANCEL;		 notificationManager.notify(0, noti);	}	//get user current	public String Rt_username(){		preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);	    return preferences.getString("username", null);	}			public String Rt_password(){		preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);	    return preferences.getString("password", null);	}		public boolean Rt_flag(){		preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);		return preferences.getBoolean("flag", false);	}}