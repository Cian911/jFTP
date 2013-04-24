package com.redpois0n;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.redpois0n.components.BaseDialog;
import com.redpois0n.renderers.ProfilesTreeRenderer;

@SuppressWarnings("serial")
public class FrameProfiles extends BaseDialog {

	private JPanel contentPane;
	private JTree tree;
	private DefaultTreeModel treeModel;
	private JTextField txtHost;
	private JSpinner spPort;
	private JComboBox<String> cbSecurity;
	private JTextField txtUsername;
	private JPasswordField pwdPassword;
	private JTextPane txtComments;
	private JTabbedPane tabbedPane;
	private JButton btnSave;
	
	public String getHost() {
		return txtHost.getText().trim();
	}
	
	public String getPassword() {
		return new String(pwdPassword.getPassword());
	}
	
	public String getSecurity() {
		return cbSecurity.getSelectedItem().toString();
	}
	
	public int getPort() {
		return (Integer)spPort.getValue();
	}
	
	public String getUsername() {
		return txtUsername.getText().trim();
	}
	
	public String getComments() {
		return txtComments.getText().trim();
	}

	public FrameProfiles() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				btnSave.doClick();
			}
		});
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrameProfiles.class.getResource("/com/redpois0n/icons/profiles.png")));
		setTitle("Profiles");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 569, 411);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel lblChooseProfile = new JLabel("Choose profile:");
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		JSeparator separator = new JSeparator();
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
				String name = node.toString().trim();
				
				if (!name.equalsIgnoreCase("My profiles")) {
					Profile p = Profiles.getProfiles().get(name);
					
					Frame.getInstance().connect(false, p);
				}
			}
		});
		btnConnect.setIcon(new ImageIcon(FrameProfiles.class.getResource("/com/redpois0n/icons/connect.png")));
		
		btnSave = new JButton("Save");
		btnSave.setIcon(new ImageIcon(FrameProfiles.class.getResource("/com/redpois0n/icons/save.png")));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		
		JButton btnAddProfile = new JButton("Add");
		btnAddProfile.setIcon(new ImageIcon(FrameProfiles.class.getResource("/com/redpois0n/icons/server_plus.png")));
		btnAddProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String input = Dialogs.showInput("Input name of new profile", "New Profile");
				if (input == null || input.trim().length() == 0) {
					return;
				}
				Profiles.addProfile(input.trim(), new Profile(input.trim()));
				
				load();
			}
		});
		
		JButton btnRemoveProfile = new JButton("Remove");
		btnRemoveProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
				String name = node.toString().trim();
				
				if (!name.equalsIgnoreCase("My profiles")) {
					Profiles.remove(name);
					treeModel.removeNodeFromParent(node);
				}
			}
		});
		btnRemoveProfile.setIcon(new ImageIcon(FrameProfiles.class.getResource("/com/redpois0n/icons/server_minus.png")));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE))
						.addComponent(separator, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 551, GroupLayout.PREFERRED_SIZE)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(btnAddProfile)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnRemoveProfile)
							.addPreferredGap(ComponentPlacement.RELATED, 227, Short.MAX_VALUE)
							.addComponent(btnConnect)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSave))
						.addComponent(lblChooseProfile))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblChooseProfile)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(scrollPane, 0, 0, Short.MAX_VALUE)
						.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 295, Short.MAX_VALUE))
					.addGap(18)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAddProfile)
						.addComponent(btnRemoveProfile)
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnConnect))
					.addContainerGap())
		);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("General", null, panel, null);
		
		JLabel lblHost = new JLabel("Host:");
		
		txtHost = new JTextField();
		txtHost.setColumns(10);
		
		JLabel lblPort = new JLabel("Port:");
		
		spPort = new JSpinner();
		spPort.setModel(new SpinnerNumberModel(21, 1, 65535, 1));
		
		JLabel lblSecurity = new JLabel("Security:");
		
		cbSecurity = new JComboBox<String>();
		cbSecurity.setModel(new DefaultComboBoxModel<String>(new String[] {"Normal", "FTPS", "FTPES"}));
		
		JSeparator separator_1 = new JSeparator();
		
		JLabel lblUsername = new JLabel("Username:");
		
		txtUsername = new JTextField();
		txtUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		
		pwdPassword = new JPasswordField();
		
		JLabel lblComments = new JLabel("Comments:");
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblHost)
								.addComponent(lblSecurity))
							.addGap(38)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(txtHost, GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblPort)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(spPort, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE))
								.addComponent(cbSecurity, 0, 286, Short.MAX_VALUE)))
						.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 366, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblUsername)
								.addComponent(lblPassword))
							.addGap(29)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(pwdPassword, GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
								.addComponent(txtUsername, GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)))
						.addComponent(lblComments)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 367, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblHost)
						.addComponent(txtHost, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(spPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPort))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(cbSecurity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSecurity))
					.addGap(18)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 9, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUsername)
						.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(pwdPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPassword))
					.addGap(37)
					.addComponent(lblComments)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		txtComments = new JTextPane();
		scrollPane_1.setViewportView(txtComments);
		panel.setLayout(gl_panel);
		
		tree = new JTree();
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {		
				if (tree.getSelectionPath() == null) {
					return;
				}
				
				TreePath old = arg0.getOldLeadSelectionPath();
				
				if (old != null) {
					save(old.getLastPathComponent().toString().trim());
				}
				
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
				String name = node.toString().trim();
				
				enableAll(!name.equalsIgnoreCase("My profiles"));
				
				if (!name.equalsIgnoreCase("My profiles")) {
					Profile pr = Profiles.getProfiles().get(name);
					
					if (pr != null) {
						txtHost.setText(pr.getHost());
						pwdPassword.setText(pr.getPassword());
						spPort.setValue(pr.getPort());
						txtUsername.setText(pr.getUsername());
						txtComments.setText(pr.getComment());
						cbSecurity.setSelectedItem(pr.getSecurity());
					}
				}
				
				if (cbSecurity.getSelectedIndex() == -1) {
					cbSecurity.setSelectedIndex(0);
				}
			}
		});
		tree.setCellRenderer(new ProfilesTreeRenderer());
		tree.setShowsRootHandles(true);
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("My profiles")
		));
		treeModel = (DefaultTreeModel)tree.getModel();
		scrollPane.setViewportView(tree);
		contentPane.setLayout(gl_contentPane);
		
		load();
		
		enableAll(false);
	}
	
	public void load() {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot(); 
		
		root.removeAllChildren();
		
		treeModel.reload();
		
		HashMap<String, Profile> map = Profiles.getProfiles();
		
		for (int i = 0; i < map.size(); i++) {			
			treeModel.insertNodeInto(new DefaultMutableTreeNode(map.keySet().toArray(new String[0])[i]), root, root.getChildCount());
		}
		
		tree.expandRow(0);
		
	}
	
	public void enableAll(boolean b) {
		enableAll(b, tabbedPane.getComponents());
	}
	
	public void enableAll(boolean b, Component[] components) {
		for (Component c : components) {
			if (c instanceof JComponent) {
				JComponent con = (JComponent)c;
				enableAll(b, con.getComponents());
				con.setEnabled(b);
			}
		}
	}
	
	public void save() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
		String name = node.toString().trim();
		
		if (!name.equalsIgnoreCase("My profiles")) {
			save(name);
			
			Frame.getInstance().updateProfiles();
		}
	}
	
	public void save(String name) {
		Profile pr = Profiles.getProfiles().get(name);
		
		if (pr != null) {
			pr.setHost(getHost());
			pr.setPort(getPort());
			pr.setComment(getComments());
			pr.setSecurity(getSecurity());
			pr.setUsername(getUsername());
			pr.setPassword(getPassword());
			pr.setName(name);
			
			Profiles.addProfile(pr.getName(), pr);
		}		
	}

}
