/*
 * Hay dos maneras de controlar las llamadas entrantes;
 * la primera es usando TelephonyManager y una clase
 * PhoneStateListener.
 * La segunda es usando un broadcast receiver para 
 * TelephonyManager.ACTION_PHONE_STATE_CHANGED.
 * Ambas son equivalentes.
 */
package activity;

import java.util.ArrayList;

import service.CallService;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;

import com.android.prtlfcalls.R;
import components.Preferences;

import dbo.TelefonoDao;

public class MainActivity extends Activity {

	private CheckBox CheckedStartOnBoot;
	private ListView lvBlockedPhones;
	private Switch swServiceState;
	private Preferences preferences;
	private TelefonoDao tlfDao;
	private Button btnNuevoTelefono;
	private ArrayList<String> arrayTlf;
	private ArrayAdapter lvAdapter;
	private EditText etDialogInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		load_views();
		registerForContextMenu(lvBlockedPhones);
		tlfDao = new TelefonoDao(this);

		arrayTlf = tlfDao.listarNumTelefonos();

		lvAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				arrayTlf);
		lvBlockedPhones.setAdapter(lvAdapter);

		btnNuevoTelefono.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				etDialogInput = new EditText(MainActivity.this);
				etDialogInput.setInputType(InputType.TYPE_CLASS_PHONE);
				
				/*
				 * pide al usuario un numero telefonico y lo guarda en un 
				 * EditText (etDialogInput)
				 */
				AlertDialog.Builder newNumberDialog = new AlertDialog.Builder(MainActivity.this).
						setMessage("Añadir Numero de Telefono").
						setView(etDialogInput).
						setPositiveButton("OK", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String inputPhone = etDialogInput.getText().toString();
								tlfDao.insertarNumTelefono(inputPhone);
								arrayTlf.add(inputPhone);
								lvAdapter.notifyDataSetChanged();
							}
						});
				newNumberDialog.show();
			}
		});
		
	}

	private void load_views() {
		CheckedStartOnBoot = (CheckBox) findViewById(R.id.cbStartOnBoot);
		swServiceState = (Switch) findViewById(R.id.swServiceState);
		lvBlockedPhones = (ListView) findViewById(R.id.lvBlockedPhones);
		btnNuevoTelefono = (Button) findViewById(R.id.btnNuevoTelefono);

		// en el inicio de la app el estado de los botones 
		// depende de la preferencia SharedPrefs.
		preferences = new Preferences(getApplicationContext());
		CheckedStartOnBoot.setChecked(preferences.isRunAtStartup());
		swServiceState.setChecked(CallService.serviceRunning);
	}

	public void onCheckboxClicked(View view) {
		if (((CheckBox) view).isChecked()) {
			// abrir editor de preferencias y activar arranque al inicio
			preferences.setRunAtStartup(true);
		}
		else {
			preferences.setRunAtStartup(false);
		}
	}

	public void onToggleClicked(View view) {
		if (((Switch) view).isChecked()) {
			if (!(CallService.serviceRunning)) {
				//Log.d("CALLBLOCKER", "INICIO EL SERVICIO");
				Intent callServiceIntent = new Intent(this, CallService.class);
				startService(callServiceIntent);
			}
		} else {
			if (CallService.serviceRunning) {
				//Log.d("CALLBLOCKER", "PARO EL SERVICIO");
				Intent callServiceIntent = new Intent(this, CallService.class);
				stopService(callServiceIntent);
			}
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	/*
	 * acciones del menu contextual, de momento
	 * solo hay una; eliminar.
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		AdapterContextMenuInfo menuInfo;

		switch (item.getItemId()) {
		case R.id.eliminarTelefono:
			menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
			//Log.d("RssReader", "eliminar " + feed);
			String tlf = (String) lvAdapter.getItem(menuInfo.position);
			if (tlfDao.eliminarNumTelefono(tlf))
				Log.d("BlockCaller", "exito pimo");

			lvAdapter.remove(lvAdapter.getItem(menuInfo.position));

			lvAdapter.notifyDataSetChanged();
			break;
		}
		return true;
	}
}