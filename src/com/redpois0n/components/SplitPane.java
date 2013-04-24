package com.redpois0n.components;

import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

@SuppressWarnings("serial")
public class SplitPane extends JSplitPane {
	
	public SplitPane() {
		super.setUI(new BasicSplitPaneUI() {
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    public void setBorder(Border b) {
                    	
                    }
                };
            }
        });
		super.setBorder(null);
	}

}
