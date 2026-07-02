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

public class ChangeReservationDialog extends Dialog implements ActionListener, WindowListener, ItemListener {

	boolean canceled; // 変更キャンセルステータス（キャンセル：true）
	boolean deleteRequested; // 削除要求ステータス（削除：true）
	ReservationControl rc; // ReservationControlインスタンス保存用

	// パネル
	Panel panelNorth;
	Panel panelNorthSub1;
	Panel panelNorthSub2;
	Panel panelCenter;
	Panel panelSouth;

	// 入力用コンポーネント
	TextField tfReservationId; // 変更対象予約IDのテキストフィールド
	Button buttonLoad; // 予約情報読込ボタン
	Button buttonDelete; // 削除ボタン
	ChoiceFacility choiceFacility; // 教室選択用ボックス
	TextField tfYear, tfMonth, tfDay; // 年月日のテキストフィールド
	ChoiceHour startHour; // 予約開始時間（時）の選択ボックス
	ChoiceMinute startMinute; // 予約開始時間（分）の選択ボックス
	ChoiceHour endHour; // 予約終了時間（時）の選択ボックス
	ChoiceMinute endMinute; // 予約終了時間（分）の選択ボックス

	// メッセージ表示ラベル
	Label lblMessage;

	// ボタン
	Button buttonOK; // OKボタン（変更実行）
	Button buttonCancel; // キャンセルボタン

	public ChangeReservationDialog(Frame owner, ReservationControl rc) {
		super(owner, "予約変更", true);

		this.rc = rc;
		canceled = true;

		// 教室選択ボックスの生成
		List<String> facilityId = new ArrayList<String>();
		facilityId = rc.getFacilityId();
		choiceFacility = new ChoiceFacility(facilityId);

		// テキストフィールドの生成
		tfReservationId = new TextField("", 5);
		tfYear = new TextField("", 4);
		tfMonth = new TextField("", 2);
		tfDay = new TextField("", 2);

		// 読込ボタンの生成
		buttonLoad = new Button("読込");

		// 開始・終了時刻選択ボックスの生成
		startHour = new ChoiceHour();
		startMinute = new ChoiceMinute();
		endHour = new ChoiceHour();
		endMinute = new ChoiceMinute();

		// メッセージラベルの生成
		lblMessage = new Label("変更する予約IDを入力して「読込」を押してください。");

		// ボタンの生成
		buttonOK = new Button("変更実行");
		buttonCancel = new Button("キャンセル");
		buttonDelete = new Button("削除");

		deleteRequested = false;

		// パネルの生成
		panelNorth = new Panel(new BorderLayout());
		panelNorthSub1 = new Panel();
		panelNorthSub2 = new Panel();
		panelCenter = new Panel();
		panelSouth = new Panel(new BorderLayout());

		// panelNorthSub1: 予約ID入力・読込ボタン
		panelNorthSub1.add(new Label("対象の予約ID："));
		panelNorthSub1.add(tfReservationId);
		panelNorthSub1.add(buttonLoad);

		// panelNorthSub2: 教室・日付入力
		panelNorthSub2.add(new Label("教室　"));
		panelNorthSub2.add(choiceFacility);
		panelNorthSub2.add(new Label("予約日"));
		panelNorthSub2.add(tfYear);
		panelNorthSub2.add(new Label("年"));
		panelNorthSub2.add(tfMonth);
		panelNorthSub2.add(new Label("月"));
		panelNorthSub2.add(tfDay);
		panelNorthSub2.add(new Label("日"));

		// panelNorthにSub1とSub2を追加
		panelNorth.add(panelNorthSub1, BorderLayout.NORTH);
		panelNorth.add(panelNorthSub2, BorderLayout.CENTER);

		// panelCenter: 開始・終了時刻
		panelCenter.add(new Label("変更後の予約時間"));
		panelCenter.add(startHour);
		panelCenter.add(new Label("時"));
		panelCenter.add(startMinute);
		panelCenter.add(new Label("分～"));
		panelCenter.add(endHour);
		panelCenter.add(new Label("時"));
		panelCenter.add(endMinute);
		panelCenter.add(new Label("分"));

		// panelSouthの上のほうにメッセージラベル、下のほうにボタン用パネルを追加
		Panel buttonPanel = new Panel();
		buttonPanel.add(buttonCancel);
		buttonPanel.add(new Label("   "));
		buttonPanel.add(buttonOK);
		buttonPanel.add(new Label("   "));
		buttonPanel.add(buttonDelete);

		// メッセージラベルを中央寄せで配置するためのサブパネル
		Panel msgPanel = new Panel();
		msgPanel.add(lblMessage);

		panelSouth.add(msgPanel, BorderLayout.NORTH);
		panelSouth.add(buttonPanel, BorderLayout.SOUTH);

		// ダイアログ全体のレイアウト設定
		setLayout(new BorderLayout());
		add(panelNorth, BorderLayout.NORTH);
		add(panelCenter, BorderLayout.CENTER);
		add(panelSouth, BorderLayout.SOUTH);

		// リスナの登録
		addWindowListener(this);
		buttonLoad.addActionListener(this);
		buttonOK.addActionListener(this);
		buttonCancel.addActionListener(this);
		buttonDelete.addActionListener(this);
		choiceFacility.addItemListener(this);
		startHour.addItemListener(this);
		endHour.addItemListener(this);

		// 初期状態で時間選択肢をセット
		resetTimeRange(choiceFacility.getSelectedItem());

		// 境界サイズの設定とサイズ固定化
		this.setBounds(100, 100, 520, 200);
		setResizable(false);
	}

	private void resetTimeRange(String facility) {
		int[][] availableTime;
		availableTime = rc.getAvailableTime(facility);
		startHour.resetRange(availableTime[0][0], availableTime[1][0]);
		endHour.resetRange(availableTime[0][0], availableTime[1][0]);
	}

	private void loadReservationInfo() {
		String idStr = tfReservationId.getText().trim();
		if (idStr.isEmpty()) {
			lblMessage.setText("予約IDを入力してください。");
			return;
		}
		if (!idStr.matches("[0-9]+")) {
			lblMessage.setText("予約IDは半角数字で入力してください。");
			return;
		}

		ReservationControl.ReservationInfo info = rc.getReservationForUpdate(idStr);
		if (info == null) {
			lblMessage.setText("指定された予約IDが存在しません。");
			return;
		}
		System.out.println("Comparing: DB UserID = [" + info.userId + "], LoggedIn UserID = [" + rc.reservationUserID + "]");
		if (!info.userId.trim().equalsIgnoreCase(rc.reservationUserID.trim())) {
			lblMessage.setText("自分以外の予約は変更できません。");
			return;
		}

		// 選択教室をセットし、時間範囲を初期化
		choiceFacility.select(info.facilityId);
		resetTimeRange(info.facilityId);

		// 日付 (yyyy-MM-dd) のパースとセット
		String[] dateParts = info.day.split("-");
		if (dateParts.length == 3) {
			tfYear.setText(dateParts[0]);
			tfMonth.setText(String.valueOf(Integer.parseInt(dateParts[1])));
			tfDay.setText(String.valueOf(Integer.parseInt(dateParts[2])));
		}

		// 開始時刻 (hh:mm:ss) のパースとセット
		String[] startParts = info.startTime.split(":");
		if (startParts.length >= 2) {
			startHour.select(String.format("%02d", Integer.parseInt(startParts[0])));
			startMinute.select(startParts[1]);
		}

		// 終了時刻 (hh:mm:ss) のパースとセット
		String[] endParts = info.endTime.split(":");
		if (endParts.length >= 2) {
			endHour.select(String.format("%02d", Integer.parseInt(endParts[0])));
			endMinute.select(endParts[1]);
		}

		lblMessage.setText("予約情報を読み込みました。変更内容を入力してください。");
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == choiceFacility) {
			String startTime = startHour.getSelectedItem();
			String endTime = endHour.getSelectedItem();
			resetTimeRange(choiceFacility.getSelectedItem());
			if (Integer.parseInt(startTime) < Integer.parseInt(startHour.getFirst())) {
				startTime = startHour.getFirst();
			}
			if (Integer.parseInt(endTime) > Integer.parseInt(endHour.getLast())) {
				endTime = endHour.getLast();
			}
			startHour.select(startTime);
			endHour.select(endTime);
		} else if (e.getSource() == startHour) {
			int start = Integer.parseInt(startHour.getSelectedItem());
			String endTime = endHour.getSelectedItem();
			endHour.resetRange(start, Integer.parseInt(endHour.getLast()));
			if (Integer.parseInt(endTime) >= start) {
				endHour.select(endTime);
			}
		} else if (e.getSource() == endHour) {
			int end = Integer.parseInt(endHour.getSelectedItem());
			String startTime = startHour.getSelectedItem();
			startHour.resetRange(Integer.parseInt(startHour.getFirst()), end);
			if (Integer.parseInt(startTime) <= end) {
				startHour.select(startTime);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttonCancel) {
			setVisible(false);
			dispose();
		} else if (e.getSource() == buttonLoad) {
			loadReservationInfo();
		} else if (e.getSource() == buttonOK) {
			canceled = false;
			setVisible(false);
			dispose();
		} else if (e.getSource() == buttonDelete) {
			canceled = false;
			deleteRequested = true;
			setVisible(false);
			dispose();
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		setVisible(false);
		dispose();
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
