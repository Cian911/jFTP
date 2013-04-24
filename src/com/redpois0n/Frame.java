package com.redpois0n;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import com.redpois0n.components.BaseFrame;
import com.redpois0n.components.LocalFileJTable;
import com.redpois0n.components.LogTextPane;
import com.redpois0n.components.RemoteFileJTable;
import com.redpois0n.components.Separator;
import com.redpois0n.components.SplitPane;
import com.redpois0n.components.TransferJTable;
import com.redpois0n.ftp.Connection;
import com.redpois0n.ftp.Transfer;

@SuppressWarnings("serial")
public class Frame extends BaseFrame implements FileChanged {

	private static Connection con;
	private static Frame instance;
	private JPanel contentPane;
	private LocalFileJTable localTable;
	private RemoteFileJTable remoteTable;
	private JTextField txtLocalLocation;
	private JTextField txtRemoteLocation;
	private TransferJTable transferTable;
	private JTextField txtHost;
	private JTextField txtUsername;
	private JPasswordField txtPass;
	private JSpinner spPort;
	private JButton btnConnect;
	private JMenuItem mntmConnect;
	private JMenuItem mntmDisconnect;
	private JMenuItem mntmCloseConnection;
	private LogTextPane txtLog;
	private JScrollPane spLog;
	private SplitPane spLogSp;
	private SplitPane spSpTransfer;
	private JToggleButton tglbtnLog;
	private JToggleButton tglbtnTransfer;
	private JMenu mnProfiles;

	public static Frame getInstance() {
		return instance;
	}
	
	public static Connection getConnection() {
		return con;
	}
	
	public static void setConnection(Connection c) {
		con = c;
	}

	public TransferJTable getTransferTable() {
		return transferTable;
	}
	
	public String getRemotePath() {
		return txtRemoteLocation.getText().trim();
	}

	public String getLocalPath() {
		return txtLocalLocation.getText().trim();
	}

	public RemoteFileJTable getRemoteTable() {
		return remoteTable;
	}

	public String getHost() {
		return txtHost.getText().trim();
	}

	public String getUsername() {
		return txtUsername.getText().trim();
	}

	public String getPass() {
		return new String(txtPass.getPassword());
	}

	public int getPort() {
		return (Integer) spPort.getValue();
	}
	
	public LogTextPane getLog() {
		return txtLog;
	}

	public Frame() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Frame.class.getResource("/com/redpois0n/icons/icon.png")));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				Main.exit();
			}
		});
		instance = this;
		setTitle(Main.getTitle());
		setBounds(100, 100, 798, 751);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmProfiles = new JMenuItem("Profiles");
		mntmProfiles.setIcon(new ImageIcon(Frame.class.getResource("/com/redpois0n/icons/profiles.png")));
		mntmProfiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FrameProfiles frame = new FrameProfiles();
				frame.setVisible(true);
			}
		});
		mnFile.add(mntmProfiles);
		
		mnFile.addSeparator();
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Main.exit();
			}
		});
		mntmExit.setIcon(new ImageIcon(Frame.class.getResource("/com/redpois0n/icons/door-open-out.png")));
		mnFile.add(mntmExit);
		
		JMenu mnConnection = new JMenu("Connection");
		menuBar.add(mnConnection);
		
		mntmConnect = new JMenuItem("Connect");
		mntmConnect.setIcon(new ImageIcon(Frame.class.getResource("/com/redpois0n/icons/connect.png")));
		mntmConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connect(true, null);
			}
		});
		mnConnection.add(mntmConnect);
		
		mnConnection.addSeparator();
		
		mntmDisconnect = new JMenuItem("Disconnect");
		mntmDisconnect.setIcon(new ImageIcon(Frame.class.getResource("/com/redpois0n/icons/disconnect.png")));
		mntmDisconnect.setEnabled(false);
		mntmDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connect(true, null);
			}
		});
		mnConnection.add(mntmDisconnect);
		
		mntmCloseConnection = new JMenuItem("Close connection");
		mntmCloseConnection.setIcon(new ImageIcon(Frame.class.getResource("/com/redpois0n/icons/close_connection.png")));
		mntmCloseConnection.setEnabled(false);
		mntmCloseConnection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connect(false, null);
			}
		});
		mnConnection.add(mntmCloseConnection);
		
		mnProfiles = new JMenu("Profiles");
		menuBar.add(mnProfiles);
		
		JMenu mnShow = new JMenu("Show");
		menuBar.add(mnShow);
		
		JMenuItem mntmLog = new JMenuItem("Log");
		mntmLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(tglbtnLog);
			}
		});
		
		JMenuItem mntmRefresh = new JMenuItem("Refresh");
		mntmRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					localTable.reload();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
				try {
					remoteTable.reload();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		mntmRefresh.setIcon(new ImageIcon(Frame.class.getResource("/com/redpois0n/icons/update.png")));
		mnShow.add(mntmRefresh);
		
		mnShow.addSeparator();
		
		mntmLog.setIcon(new ImageIcon(Frame.class.getResource("/com/redpois0n/icons/log.png")));
		mnShow.add(mntmLog);
		
		JMenuItem mntmTransferTable = new JMenuItem("Transfer table");
		mntmTransferTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(tglbtnTransfer);
			}
		});
		mntmTransferTable.setIcon(new ImageIcon(Frame.class.getResource("/com/redpois0n/icons/transfers.png")));
		mnShow.add(mntmTransferTable);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new FrameAbout().setVisible(true);
			}
		});
		mntmAbout.setIcon(new ImageIcon(Frame.class.getResource("/com/redpois0n/icons/icon.png")));
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		spLogSp = new SplitPane();
		spLogSp.setOrientation(JSplitPane.VERTICAL_SPLIT);
		spLogSp.setResizeWeight(0.2);

		JToolBar toolBar_2 = new JToolBar();
		toolBar_2.setFloatable(false);

		JLabel lblHost = new JLabel("Host:");

		txtHost = new JTextField();
		txtHost.setColumns(10);

		JLabel lblUsername = new JLabel("Username:");

		txtUsername = new JTextField();
		txtUsername.setColumns(10);

		JLabel lblPassword = new JLabel("Password:");

		txtPass = new JPasswordField();

		JLabel lblPort = new JLabel("Port:");

		spPort = new JSpinner();
		spPort.setModel(new SpinnerNumberModel(21, 1, 65535, 1));

		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connect(true, null);
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(toolBar_2, GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblHost)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtHost, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblUsername)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblPassword)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtPass, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblPort)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(spPort, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnConnect)
					.addContainerGap(101, Short.MAX_VALUE))
				.addComponent(spLogSp, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(toolBar_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblHost)
						.addComponent(txtHost, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUsername)
						.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPassword)
						.addComponent(txtPass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPort)
						.addComponent(spPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnConnect))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(spLogSp, GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
					.addGap(0))
		);
		
		tglbtnLog = new JToggleButton("");
		tglbtnLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean b = ((JToggleButton)arg0.getSource()).isSelected();
			
				if (b) {
					spLogSp.setDividerLocation(spLogSp.getHeight() / 6);
				} else {
					spLogSp.setDividerLocation(0);
				}
			}
		});
		tglbtnLog.setSelected(true);
		tglbtnLog.setIcon(new ImageIcon(Frame.class.getResource("/com/redpois0n/icons/log.png")));
		tglbtnLog.setToolTipText("Hide/show log");
		toolBar_2.add(tglbtnLog);
		
		tglbtnTransfer = new JToggleButton("");
		tglbtnTransfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean b = ((JToggleButton)arg0.getSource()).isSelected();
				
				if (b) {
					spSpTransfer.setDividerLocation(spSpTransfer.getHeight() - spSpTransfer.getHeight() / 4);
				} else {
					spSpTransfer.setDividerLocation(10000);
				}
			}
		});
		tglbtnTransfer.setSelected(true);
		tglbtnTransfer.setIcon(new ImageIcon(Frame.class.getResource("/com/redpois0n/icons/transfers.png")));
		tglbtnTransfer.setToolTipText("Hide/show transfer table");
		toolBar_2.add(tglbtnTransfer);

		toolBar_2.addSeparator(Separator.getSeparator());
		
		spSpTransfer = new SplitPane();
		spLogSp.setRightComponent(spSpTransfer);
		spSpTransfer.setOrientation(SplitPane.VERTICAL_SPLIT);
		spSpTransfer.setResizeWeight(0.75);

		SplitPane splitPane_1 = new SplitPane();
		splitPane_1.setResizeWeight(0.5);
		spSpTransfer.setLeftComponent(splitPane_1);

		SplitPane splitPane_2 = new SplitPane();
		splitPane_2.setOrientation(SplitPane.VERTICAL_SPLIT);
		splitPane_1.setLeftComponent(splitPane_2);

		JScrollPane scrollPane = new JScrollPane();
		splitPane_2.setRightComponent(scrollPane);

		localTable = new LocalFileJTable(this);
		localTable.loadRoots();
		
		scrollPane.setViewportView(localTable);

		JToolBar localToolBar = new JToolBar();
		localToolBar.setFloatable(false);
		splitPane_2.setLeftComponent(localToolBar);

		JLabel lblLocalFolder = new JLabel("Local folder:   ");
		localToolBar.add(lblLocalFolder);

		txtLocalLocation = new JTextField();
		localToolBar.add(txtLocalLocation);
		
		localToolBar.add(Separator.getInvisibleSeparator());
		
		for (Component com : localTable.getToolbar()) {
			localToolBar.add(com);
		}

		txtLocalLocation.setEditable(false);
		txtLocalLocation.setColumns(10);

		SplitPane splitPane_3 = new SplitPane();
		splitPane_3.setOrientation(SplitPane.VERTICAL_SPLIT);
		splitPane_1.setRightComponent(splitPane_3);

		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane_3.setRightComponent(scrollPane_1);

		remoteTable = new RemoteFileJTable(this);
		scrollPane_1.setViewportView(remoteTable);

		JToolBar remoteToolBar = new JToolBar();
		remoteToolBar.setFloatable(false);
		splitPane_3.setLeftComponent(remoteToolBar);

		JLabel lblRemoteFolder = new JLabel("Remote folder:   ");
		remoteToolBar.add(lblRemoteFolder);
		txtRemoteLocation = new JTextField();
		remoteToolBar.add(txtRemoteLocation);
		
		remoteToolBar.add(Separator.getInvisibleSeparator());
		
		for (Component com : remoteTable.getToolbar()) {
			remoteToolBar.add(com);
		}
		
		txtRemoteLocation.setEditable(false);
		txtRemoteLocation.setColumns(10);

		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		spSpTransfer.setRightComponent(scrollPane_3);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(scrollPane_3, popupMenu);
		
		
		JMenuItem mntmCancel = new JMenuItem("Cancel");
		mntmCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (Transfer transfer : getSelectedTransfers()) {
					transfer.cancel();
				}
			}
		});
		mntmCancel.setIcon(new ImageIcon(Frame.class.getResource("/com/redpois0n/icons/delete.png")));
		popupMenu.add(mntmCancel);

		transferTable = new TransferJTable();
		transferTable.setRowHeight(25);
		addPopup(transferTable, popupMenu);
		
		JMenuItem mntmOpenLocalFolder = new JMenuItem("Open local folder");
		mntmOpenLocalFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (Transfer transfer : getSelectedTransfers()) {
					try {
						Desktop.getDesktop().open(transfer.getLocalFile().getParentFile());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		mntmOpenLocalFolder.setIcon(new ImageIcon(Frame.class.getResource("/com/redpois0n/icons/folder_go.png")));
		popupMenu.add(mntmOpenLocalFolder);
		scrollPane_3.setViewportView(transferTable);

		spLog = new JScrollPane();
		spLog.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		spLogSp.setLeftComponent(spLog);

		txtLog = new LogTextPane();
		spLog.setViewportView(txtLog);
		contentPane.setLayout(gl_contentPane);
		
		updateProfiles();
	}

	public void connect(boolean legal, Profile p) {
		try {
			boolean connect = btnConnect.getText().equals("Connect");
			boolean enableControls = !connect;
			
			if (connect) {
				getLog().status("Connecting to " + (p == null ? getHost() : p.getHost()));
				
				btnConnect.setText("Disconnect");
				
				if (p == null) {
					con = Connection.connect(getHost(), getUsername(), getPass(), getPort());	
				} else {
					con = Profile.connect(p);
				}
			} else {
				try {
					con.getClient().disconnect(legal);
				} catch (Exception ex) { };
				
				if (legal) {
					getLog().status("Disconnected from server");
				} else {
					getLog().error("Connection lost");
				}
				
				btnConnect.setText("Connect");
				
				
				
				txtRemoteLocation.setText("");
				getTransferTable().clear();
				getRemoteTable().clear();
			}
			
			enableControls(enableControls);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void enableControls(boolean enableControls) {
		txtHost.setEnabled(enableControls);
		txtUsername.setEnabled(enableControls);
		txtPass.setEnabled(enableControls);
		spPort.setEnabled(enableControls);
		
		mntmCloseConnection.setEnabled(!enableControls);
		mntmDisconnect.setEnabled(!enableControls);
		mntmConnect.setEnabled(enableControls);
	}

	@Override
	public void localFileChanged(File file) {
		txtLocalLocation.setText(file.getAbsolutePath());
	}

	@Override
	public void remoteFileChanged(String file) {
		txtRemoteLocation.setText(file);
	}
	
	public Transfer[] getSelectedTransfers() {
		int[] rows = transferTable.getSelectedRows();

		if (rows.length > 0) {
			Transfer[] all = Transfer.toArray();
			List<Transfer> transfers = new ArrayList<Transfer>();
			
			for (int i = 0; i < rows.length; i++) {
				int row = rows[i];
				String local = transferTable.getValueAt(row, 0).toString();
				String remote = transferTable.getValueAt(row, 2).toString();
				
				for (Transfer t : all) {
					if (t.getRemoteFile().equals(remote) && t.getLocalFile().getAbsolutePath().equals(local)) {
						transfers.add(t);
					}
				}
			}
			return transfers.toArray(new Transfer[0]);
		} else {
			return new Transfer[0];
		}
	}
	
	public void click(AbstractButton b) {
		b.doClick();
	}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	public void updateProfiles() {
		HashMap<String, Profile> map = Profiles.getProfiles();
		mnProfiles.removeAll();
		
		if (map.size() == 0) {
			JMenuItem item = new JMenuItem("No profiles");
			mnProfiles.add(item);
		} else {
			ActionListener l = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					JMenuItem item = (JMenuItem)arg0.getSource();
					
					if (btnConnect.getText().equals("Disconnect")) {
						connect(true, null);
					}
					
					connect(false, Profiles.getProfiles().get(item.getText().split(" - ")[0]));
				}				
			};
			for (String key : map.keySet()) {
				Profile profile = map.get(key);
				JMenuItem item = new JMenuItem(key + " - " + profile.getUsername() + "@" + profile.getHost());
				item.addActionListener(l);
				mnProfiles.add(item);
			}
		}
	}
}
