package com.dev.aproschenko.bluetoothterminal;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.aproschenko.bluetoothterminal.colorpicker.ColorPickerPreference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

public class TerminalActivity extends Activity
{
    private static final String TAG = "TerminalActivity";
    private static final boolean D = true;
    public static final String NOT_SET_TEXT = "-";

    private String connectedDeviceName;
    private String connectedDeviceAddress;

    private Button buttonSend;
    private EditText commandBox;
    private TextView commandsView;
    private TextView temperatura;
    private ImageView rotCircle;
    private int grad=1;
    private float act = 0, prox;

    private String commandsCache = "";
    private Integer buttonIds[] = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5};
    private ArrayList<Button> terminalButtons = new ArrayList<>();

    public static final int BTN_COUNT = 5;
    final Context context = this;

    public MainApplication getApp()
    {
        return (MainApplication) getApplication();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (D) Log.d(TAG, "++ ON START ++");

        if (!getApp().getAdapter().isEnabled())
        {
            if (D) Log.d(TAG, "++ ON START BT disabled ++");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, MainActivity.REQUEST_ENABLE_BT);
        }
        else // Otherwise, setup the chat session
        {
            if (D) Log.d(TAG, "++ ON START BT enabled ++");
            if (getApp().getConnector() == null)
            {
                if (D) Log.d(TAG, "++ ON START setupConnector() ++");
                setupConnector();
            }
            else
            {
                if (D) Log.d(TAG, "++ ON START ++, connector state " + getApp().getConnector().getState());
            }
        }
    }

    @Override
    public synchronized void onResume()
    {
        super.onResume();
        if (D) Log.d(TAG, "+ ON RESUME +");

        if (getApp().getConnector() != null)
        {
            if (D) Log.d(TAG, "+ ON RESUME +, connector state " + getApp().getConnector().getState());
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (D) Log.d(TAG, "--- ON DESTROY ---");

        getApp().removeHandler(mHandler);

        if (getApp().getConnector() != null)
        {
            if (D) Log.d(TAG, "--- ON DESTROY ---, connector state " + getApp().getConnector().getState());
            getApp().getConnector().stop();
            getApp().deleteConnector();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (D) Log.d(TAG, "-- ON STOP --");

        if (getApp().getConnector() != null)
        {
            if (D) Log.d(TAG, "-- ON STOP --, connector state " + getApp().getConnector().getState());
        }
    }

    @Override
    public synchronized void onPause()
    {
        super.onPause();
        if (D) Log.d(TAG, "- ON PAUSE -");

        if (getApp().getConnector() != null)
        {
            if (D) Log.d(TAG, "- ON PAUSE -, connector state " + getApp().getConnector().getState());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (D) Log.d(TAG, "+++ ON CREATE +++");

        Intent intent = getIntent();
        connectedDeviceName = intent.getStringExtra(MainActivity.DEVICE_NAME);
        connectedDeviceAddress = intent.getStringExtra(MainActivity.DEVICE_ADDRESS);

        setContentView(R.layout.terminal_layout);

        String title = String.format(getResources().getString(R.string.is_not_connected), connectedDeviceName);
        setTitle(title);

        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(buttonSendClick);

        commandBox = (EditText) findViewById(R.id.commandBox);
        commandsView = (TextView) findViewById(R.id.commandsView);
        temperatura = (TextView) findViewById(R.id.temp);
        rotCircle = (ImageView) findViewById(R.id.row_circle);

        commandsView.setMovementMethod(new ScrollingMovementMethod());
        commandsView.setTextIsSelectable(true);

        final Button btnKelvin = (Button) findViewById(R.id.btnKelvin);
        final Button btnCelsius = (Button) findViewById(R.id.btnCelsius);
        final Button btnFarenheit = (Button) findViewById(R.id.btnFarenheit);

        btnCelsius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCelsius.setBackgroundResource(R.drawable.round_button_selected);
                btnFarenheit.setBackgroundResource(R.drawable.round_button);
                btnKelvin.setBackgroundResource(R.drawable.round_button);
                grad=2;
            }
        });

        btnFarenheit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCelsius.setBackgroundResource(R.drawable.round_button);
                btnFarenheit.setBackgroundResource(R.drawable.round_button_selected);
                btnKelvin.setBackgroundResource(R.drawable.round_button);
                grad=3;
            }
        });

        btnKelvin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCelsius.setBackgroundResource(R.drawable.round_button);
                btnFarenheit.setBackgroundResource(R.drawable.round_button);
                btnKelvin.setBackgroundResource(R.drawable.round_button_selected);
                grad=1;
            }
        });

        for (int i = 0; i < buttonIds.length; i++)
        {
            int id = buttonIds[i];
            Button btn = (Button) findViewById(id);

            btn.setText(getApp().getTerminalCommands().get(i));
            btn.setOnLongClickListener(btnPredefinedCommandLongControlClick);
            btn.setOnClickListener(btnPredefinedCommandControlClick);
            btn.setTag(i);

            terminalButtons.add(btn);
        }

        getApp().addHandler(mHandler);
    }

    public void updateButtonCommand(int buttonIndex, String command)
    {
        Button btn = terminalButtons.get(buttonIndex);
        btn.setText(command);

        getApp().getTerminalCommands().set(buttonIndex, command);
        savePreferences();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (D) Log.d(TAG, "onActivityResult " + resultCode);

        if (requestCode == MainActivity.REQUEST_ENABLE_BT)
        {
            if (resultCode == RESULT_OK)
            {
                setupConnector();
            }
            else
            {
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void setupConnector()
    {
        if (D) Log.d(TAG, "setupConnector");

        if (getApp().getConnector() != null)
        {
            if (D) Log.d(TAG, "setupConnector connector.stop(), state " + getApp().getConnector().getState());
            getApp().getConnector().stop();
            getApp().deleteConnector();
        }

        BluetoothDevice connectedDevice = getApp().getAdapter().getRemoteDevice(connectedDeviceAddress);

        getApp().createConnector(connectedDevice.getAddress());
        getApp().getConnector().getHandlers().add(mHandler);
        getApp().getConnector().connect();
    }

    private void savePreferences()
    {
        SharedPreferences settings = getSharedPreferences(MainApplication.PREFS_FOLDER_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        for (int i = 0; i < BTN_COUNT; i++)
        {
            String key = MainApplication.PREFS_KEY_TERMINAL_COMMAND + i;
            String cmd = getApp().getTerminalCommands().get(i);
            editor.putString(key, cmd);

            if (D)
                Log.d(TAG, "save terminal key " + key + ":" + cmd);
        }

        editor.commit();
    }

    private View.OnLongClickListener btnPredefinedCommandLongControlClick = new View.OnLongClickListener()
    {
        @Override
        public boolean onLongClick(View v)
        {
            Button btn = (Button)v;
            showButtonActionDialog(btn);
            return true;
        }
    };

    private void showButtonActionDialog(Button btn)
    {
        int buttonIndex = (int)btn.getTag();
        ButtonSetupDialog newFragment = ButtonSetupDialog.newInstance(buttonIndex);
        newFragment.show(getFragmentManager(), "ButtonSetupDialog");
    }

    private View.OnClickListener btnPredefinedCommandControlClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Button btn = (Button)v;
            String command = btn.getText().toString();

            if (command.equals(TerminalActivity.NOT_SET_TEXT))
                Toast.makeText(context, getResources().getString(R.string.set_command_using_long_tap), Toast.LENGTH_SHORT).show();
            else
                sendCommand(command);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        if (D) Log.d(TAG, "onCreateOptionsMenu");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_terminal, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_close_terminal:
                closeTerminal();
                return true;
            case R.id.menu_copy_terminal:
                copyLog();
                return true;
            case R.id.menu_clear_terminal:
                clearLog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearLog()
    {
        commandsView.setText("");
        commandsCache = "";
        Toast.makeText(this, getResources().getString(R.string.terminal_cleared), Toast.LENGTH_SHORT).show();
    }

    private void copyLog()
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.setText(commandsView.getText());
        Toast.makeText(this, getResources().getString(R.string.terminal_copied), Toast.LENGTH_SHORT).show();
    }

    private void closeTerminal()
    {
        finish();
    }

    private View.OnClickListener buttonSendClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            String command = commandBox.getText().toString().trim();
            sendCommand(command);
        }
    };

    private String getFormattedDateTime()
    {
        Date date = new Date();
        return String.format("%s %s", DateFormat.getDateFormat(getApplicationContext()).format(date), DateFormat.format("H:mm:ss", date));
    }

    private void appendCommand(String command, int messageType) {
        String postfix = "";
        if (command.endsWith("\r\n"))
            postfix = " CR+LF";
        else if (command.endsWith("\r"))
            postfix = " CR";
        else if (command.endsWith("\n"))
            postfix = " LF";

        int commandIntColor = 0;
        String trimmedCommand = command.trim();
        if (trimmedCommand.equals("OK"))
            commandIntColor = 0xff009900;
        if (trimmedCommand.equals("ERROR"))
            commandIntColor = 0xff990000;
        String commandColor = "";
        if (commandIntColor != 0)
            commandColor = ColorPickerPreference.convertToRGB(commandIntColor);

        int intColor = messageType == Messages.MESSAGE_READ ? getApp().receivedMessageColor : getApp().sentMessageColor;
        String color = ColorPickerPreference.convertToRGB(intColor);
        int postfixIntColor = Color.BLUE;
        String postfixColor = ColorPickerPreference.convertToRGB(postfixIntColor);
        String author = messageType == Messages.MESSAGE_READ ? connectedDeviceName : "ME";
        String date = getFormattedDateTime();

        String coloredCommand = command;
        if (!commandColor.equals(""))
            coloredCommand = String.format("<font color='%s'>%s</font>", commandColor, command);
        String textToAdd = String.format("<font color='%s'>%s&gt; </font>%s<font color='%s'>%s</font><br/>", color, author, coloredCommand, postfixColor, postfix);
        if (getApp().showDateTimeLabels)
            textToAdd = String.format("%s %s", date, textToAdd);

        commandsCache = textToAdd + commandsCache;
        try {
            Float corrTemp = Float.parseFloat(command);
            prox = corrTemp;
            rotateTemperature();
            act = prox;
            StringBuilder sb = new StringBuilder();
            String tempo;
            float res;
            switch (grad) {
                case 1:
                    res = 323.15f / 1024.00f * corrTemp;
                    tempo = String.format("%.2f", res);
                    sb.append(tempo);
                    sb.append("°K");
                    break;

                case 2:
                    res = 50.00f / 1024.00f * corrTemp;
                    tempo = String.format("%.2f", res);
                    sb.append(tempo);
                    sb.append("°C");
                    break;

                case 3:
                    res = 122f / 1024.00f * corrTemp;
                    tempo = String.format("%.2f", res);
                    sb.append(tempo);
                    sb.append("°F");
                    break;
            }
            temperatura.setTextSize(50);
            temperatura.setText(sb.toString());
        } catch (NumberFormatException e) {
            temperatura.setTextSize(20);
            temperatura.setText(command);
        }
        commandsView.setText(Html.fromHtml(commandsCache), TextView.BufferType.SPANNABLE);
    }

    private void sendCommand(String command)
    {
        if (getApp().getConnectorState() == DeviceConnector.STATE_CONNECTED)
        {
            if (!command.equals(""))
            {
                switch (getApp().sentMessageEnding)
                {
                    case MainApplication.LINE_ENDING_CR:
                        command = command.concat("\r");
                        break;
                    case MainApplication.LINE_ENDING_LF:
                        command = command.concat("\n");
                        break;
                    case MainApplication.LINE_ENDING_CRLF:
                        command = command.concat("\r\n");
                        break;
                    default: //NONE
                        break;
                }
                getApp().getConnector().write(command);
                appendCommand(command, Messages.MESSAGE_WRITE);
                commandBox.setText("");
            } else
            {
                if (commandBox.requestFocus())
                {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        }
    }

    public void rotateTemperature(){
        long delay = 798;
        float pI,  pF;
        pI = 360.00f*act/1024.00f;
        pF = 350.00f*prox/1024.00f;
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(rotCircle, "rotation",  pI, pF);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(delay);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotateAnimation);
        animatorSet.start();

        rotateAnimation = ObjectAnimator.ofFloat(rotCircle, "rotation",  pF, pF);
        rotateAnimation.setDuration(1);
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotateAnimation);
        animatorSet.start();

    }

    private void enableControls()
    {
        boolean isConnected = getApp().getConnectorState() == DeviceConnector.STATE_CONNECTED;
        buttonSend.setEnabled(isConnected);
        commandBox.setEnabled(isConnected);

        for (Button btn : terminalButtons)
        {
            btn.setEnabled(isConnected);
        }
    }

    private final Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case Messages.MESSAGE_STATE_CHANGE:

                    if(D) Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    String messageText = "";

                    switch (msg.arg1)
                    {
                        case DeviceConnector.STATE_CONNECTED:
                            messageText = String.format(getResources().getString(R.string.connected_to), connectedDeviceName);
                            setTitle(messageText);
                            break;
                        case DeviceConnector.STATE_CONNECTING:
                            messageText = String.format(getResources().getString(R.string.connecting_to), connectedDeviceName);
                            setTitle(messageText);
                            break;
                        case DeviceConnector.STATE_NONE:
                            messageText = String.format(getResources().getString(R.string.is_not_connected), connectedDeviceName);
                            setTitle(messageText);
                            break;
                    }

                    enableControls();
                    invalidateOptionsMenu();
                    appendCommand(messageText, Messages.MESSAGE_WRITE);
                    break;

                case Messages.MESSAGE_DEVICE_NAME:
                    Toast.makeText(getApplicationContext(), String.format(getResources().getString(R.string.successfully_connected_to), connectedDeviceName), Toast.LENGTH_SHORT).show();
                    break;

                case Messages.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    appendCommand(readMessage, Messages.MESSAGE_READ);
                    break;

                case Messages.MESSAGE_CONNECTION_FAILED:
                    Toast.makeText(getApplicationContext(), String.format(getResources().getString(R.string.unable_connect_to), connectedDeviceName), Toast.LENGTH_SHORT).show();
                    break;

                case Messages.MESSAGE_CONNECTION_LOST:
                    enableControls();
                    appendCommand(String.format(getResources().getString(R.string.connection_was_lost), connectedDeviceName), Messages.MESSAGE_READ);
                    Toast.makeText(getApplicationContext(), String.format(getResources().getString(R.string.connection_was_lost), connectedDeviceName), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
