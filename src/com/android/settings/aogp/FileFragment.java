package com.android.settings.aogp;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import com.android.settings.aogp.adapter.FileBaseAdapter;
import com.android.settings.R;
import com.android.settings.utils.CMDProcessor;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class FileFragment extends Fragment implements OnItemClickListener{

	private ListView mList;
	private FileBaseAdapter mAdapter;
	private Context mContext;
	private String SdPath;
	private File[] filesList;
	private static File RECOVERY_DIR = new File("/cache/recovery");
	private static File COMMAND_FILE = new File(RECOVERY_DIR, "command");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		SdPath = Environment.getExternalStorageDirectory().getAbsolutePath();

		Bundle bundle = this.getArguments();
		if(bundle != null) {
			String fPath = bundle.getString("FILEPATH","none");
			if(fPath.equals("none")) {
				filesList = new File(SdPath).listFiles();
			} else {
				filesList = new File(fPath).listFiles();
			}
		}else {
			filesList = new File(SdPath).listFiles();
		}
		Arrays.sort(filesList);
		
		setRetainInstance(true);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.layout_list_file, container,false);
		mList = (ListView) v.findViewById(R.id.list);
		TextView emptyView = (TextView) v.findViewById(R.id.empty);
		mAdapter = new FileBaseAdapter(mContext, filesList);
		mList.setAdapter(mAdapter);
		mList.setEmptyView(emptyView);
		mList.setOnItemClickListener(this);
		return v;
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View parent, final int position, long id) {
		// TODO Auto-generated method stub
		if(filesList[position].isDirectory()) {
			File[] childs = filesList[position].listFiles();
			Arrays.sort(childs);
			Fragment fragment = new FileFragment();
			Bundle bundle = new Bundle();
			bundle.putString("FILEPATH", filesList[position].getAbsolutePath());
			fragment.setArguments(bundle);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
		    // This adds the newly created Preference fragment to my main layout, shown below
            ft.replace(R.id.activity_container,fragment);
			ft.commit();
		}else if(filesList[position].getName().endsWith(".apk")) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(filesList[position]), "application/vnd.android.package-archive");
			startActivity(intent);  
		}else if(filesList[position].getName().endsWith(".zip")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle(filesList[position].getName());
			builder.setItems(R.array.flash_dialog, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					bootCommand("--update_package="+filesList[position].getAbsolutePath());
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
			
		}else if(filesList[position].getName().endsWith(".img")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle(filesList[position].getName());
			builder.setItems(R.array.install_image_dialog, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					switch(which) {
					case 0:
						installBoot(filesList[position].getAbsolutePath());
						break;
					case 1:
						installRecovery(filesList[position].getAbsolutePath());
						break;
					}
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}
	
	
	public void bootCommand(String command) {
		if(!RECOVERY_DIR.exists()) {
			RECOVERY_DIR.mkdirs();
		}
		if(COMMAND_FILE.exists()) {
			COMMAND_FILE.delete();
		}
		CMDProcessor.runSuCommand("echo "+command+" > "+COMMAND_FILE.getAbsolutePath());
		CMDProcessor.runSuCommand("reboot recovery");
	}
	
	public void installBoot(String bootPath) {
		CMDProcessor.runSuCommand("dd if="+bootPath+" of=/dev/block/platform/msm_sdcc.1/by-name/boot");
		CMDProcessor.runSuCommand("reboot");
	}
	
	public void installRecovery(String recoveryPath) {
		CMDProcessor.runSuCommand("dd if="+recoveryPath+" of=/dev/block/platform/msm_sdcc.1/by-name/recovery");
		CMDProcessor.runSuCommand("reboot");
	}

}
