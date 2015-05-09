/*
*
Author: Jagadish Shivanna
References: https://developer.android.com/guide/topics/connectivity/bluetooth-le.html
**Displays scanned bluetooth smart sensors as books category**
*
*/
package com.example.jagadish.findbooks;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.*;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.pm.PackageManager;
import android.widget.Toast;
import android.util.Log;
import java.util.ArrayList;

//Activity for displaying scannded categories
public class ScanActivity extends ListActivity {

    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private static final String TAG = "ScanAct";

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 20 seconds.
    private static final long SCAN_PERIOD = 15000;
    public String name = "";
    public String address = "";
    public String distance = "";
    public ArrayList<BluetoothDevice> mLeDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_scan);
        //ActionBar actionBar = getActionBar();
       // android.app.ActionBar actionBar = getActionBar();
       // actionBar.setTitle("BLE Device Scan");
       // getActionBar().setTitle("BLE Device Scan");
        //getActionBar().setDisplayShowTitleEnabled(true);
        mHandler = new Handler();
        //boolean apiJBorAbove = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2 ? true
          //      : false;
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not supported", Toast.LENGTH_SHORT).show();
            //  finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
       final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.menu_scan:
                mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }        return super.onOptionsItemSelected(item);

    }

    //Scan functions
    @Override
    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        setListAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
       mLeDeviceListAdapter.clear();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
        //name = device.getName();
        //address = device.getAddress();
        Log.d(TAG, "Pos:" + position);
        if (device == null) return;
       final Intent intent = new Intent(this, ScanBooksActivity.class);
       // intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
        //intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());

        intent.putExtra(ScanBooksActivity.EXTRAS_DEVICE_NAME, name);
        intent.putExtra(ScanBooksActivity.EXTRAS_DEVICE_ADDRESS, address);
        if (mScanning) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanning = false;
        }
       startActivity(intent);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);
            Log.d(TAG, "Starting Scan");
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            Log.d(TAG, "After Starting Scan");
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {

        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = ScanActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if(!mLeDevices.contains(device)) {
                mLeDevices.add(device);
                Log.d(TAG, "AL:"+mLeDevices);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                //view = mInflator.inflate(R.layout.activity_scan, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            Log.d(TAG, "i:"+i);
            name = device.getName();
            address = device.getAddress();
            switch(address) {
                case "78:A5:04:8C:2A:D0":
                    name = "Sports";
                    break;
                case "B4:99:4C:64:BC:CE":
                    name = "Computer";
                    break;
                case "B4:99:4C:64:17:C1":
                    name = "Electronics";
                    break;
                case "78:A5:04:8C:15:23":
                    name = "Literature";
                    break;
                default:
                    name = "Others";
                    break;
            }

           // final String name = device.getName();

            Log.d(TAG, "NAME:"+name);
            if (name != null && name.length() > 0) {
                if (!viewHolder.deviceName.getText().equals(name))
                    viewHolder.deviceName.setText(name);
            }
            else {
                viewHolder.deviceName.setText(R.string.unknown_device);
            }
                //viewHolder.deviceAddress.setText(address);

            return view;
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
              //  Log.d(TAG, "IN LeScanCallback");

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanBytes) {
                    Log.d(TAG, "IN OnLeScan");
                    Log.d("Jaggi", Integer.toString(mLeDevices.size()));
                    //Log.d(TAG, "New LE Device: " + name + " @ " + rssi);
                    //ScanRecord scanRecord = ScanRecord.parseFromBytes(scanBytes);
                    //Log.d(TAG,"TX Power: " + name + "/" +  scanBytes);

                   //Log.d(TAG,"Distance: " + name + "///" +  distance);
                    //ScanRecord scanRecord = ScanRecord.parseFromBytes(scanBytes);
                    //if (leScanMatches(scanRecord)) {
                       // final ScanResult scanResult = new ScanResult(device, scanRecord, rssi, SystemClock.elapsedRealtimeNanos());
                       // final int txPower = getTxPowerLevel(scanResult);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLeDeviceListAdapter.addDevice(device);
                                mLeDeviceListAdapter.notifyDataSetChanged();
                               // mLeDeviceListAdapter.add(scanResult, txPower, DEVICE_LIFETIME_SECONDS);
                            }
                        });

                }
            };

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }


} //End ScanActivity