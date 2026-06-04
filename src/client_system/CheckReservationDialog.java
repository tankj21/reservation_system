package client_system;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;


public class CheckReservationDialog extends Dialog implements ActionListener, WindowListener, ItemListener{
	boolean canceled;									//表示用のboolean
	
	ReservationControl rc;
	
	TextField tfYear, tfMonth, tfDays;
	ChoiceFacility choiceFacility;
	
	Button buttonCancel;
	Button buttonOK;
	
	Panel panelNorth;
	Panel panelCenter;
	Panel panelSouth;
	
	public CheckReservationDialog ( Frame owner, ReservationControl rc) {
		super( owner, "教室確認", true);
		
		this.rc = rc;
		canceled = true;
		
		List<String> facilityId = new ArrayList<String>();
		facilityId = rc.getFacilityId();
		choiceFacility = new ChoiceFacility( facilityId);
		
		tfYear = new TextField("", 4);
		tfMonth = new TextField("", 2);
		tfDays = new TextField("",2);
		
		buttonOK = new Button("予約確認");
		buttonCancel = new Button("キャンセル");
		
		panelNorth = new Panel();
		panelCenter = new Panel();
		panelSouth = new Panel();
	
		
		panelNorth.add( new Label("教室 "));
		panelNorth.add( choiceFacility);
		panelNorth.add( new Label("  "));
		panelNorth.add( tfYear);
		panelNorth.add( new Label("年 "));
		panelNorth.add( tfMonth);
		panelNorth.add( new Label("月 "));
		panelNorth.add( tfDays);
		panelNorth.add( new Label("日 "));
		
		panelSouth.add( buttonCancel);
		panelSouth.add( new Label("   "));
		panelSouth.add( buttonOK);
		
		
		setLayout( new BorderLayout());
		add( panelNorth,	BorderLayout.NORTH);
		add( panelCenter,	BorderLayout.CENTER);
		add( panelSouth,	BorderLayout.SOUTH);
		
		
		addWindowListener(this);
		buttonOK.addActionListener(this);
		buttonCancel.addActionListener(this);
		choiceFacility.addItemListener(this);
		
		
		this.setBounds(100, 100, 500, 150);
		setResizable(false);
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		setVisible( false);
		dispose();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		if( e.getSource() == buttonCancel) {
			setVisible( false);
			dispose();
		} else if( e.getSource() == buttonOK) {
			canceled = false;
			setVisible( false);
			dispose();
		}
	}
}
		
	
