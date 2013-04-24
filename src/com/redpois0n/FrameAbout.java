package com.redpois0n;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.redpois0n.components.BaseDialog;

@SuppressWarnings("serial")
public class FrameAbout extends BaseDialog {

	public FrameAbout() {
		setTitle("About");
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrameAbout.class.getResource("/com/redpois0n/icons/icon.png")));
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 343, 217);
		
		JLabel lblHttpredpoisncom = new JLabel("http://redpois0n.com");
		lblHttpredpoisncom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Util.openUrl("http://redpois0n.com");
			}
		});
		lblHttpredpoisncom.setForeground(Color.BLUE);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 316, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblHttpredpoisncom)
							.addPreferredGap(ComponentPlacement.RELATED, 140, Short.MAX_VALUE)
							.addComponent(btnOk, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblHttpredpoisncom)
						.addComponent(btnOk))
					.addContainerGap())
		);
		
		JTextPane txtpnWrittenByRedpoisn = new JTextPane();
		txtpnWrittenByRedpoisn.setText("Written by redpois0n in Java\r\nBased on ftp4j by sauron software\r\nProject started January 2013");
		txtpnWrittenByRedpoisn.setEditable(false);
		scrollPane.setViewportView(txtpnWrittenByRedpoisn);
		getContentPane().setLayout(groupLayout);

	}

}
