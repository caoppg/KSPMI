package halfback.cmpm.controller;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import halfback.cmpm.view.ChronicleDescription;
import halfback.cmpm.view.ChronicleView;

public class ChronicleViewListener implements InternalFrameListener {
	
	private final ChronicleDescription _description;
	private final ChronicleTableModel _model;
	private int openedFrames = 0;
	
	public ChronicleViewListener(ChronicleDescription description, ChronicleTableModel model) {
		_description = description;
		_model = model;
	}

	@Override
	public void internalFrameActivated(InternalFrameEvent arg0) {
		ChronicleView selected = (ChronicleView) arg0.getSource();
		_description.setCurrent(_model.getEntry(selected.getId()).getChronicle());
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent arg0) {
		openedFrames--;
		if (0 == openedFrames) {
			_description.clean();
		}
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent arg0) {
		System.out.println("Closing I guess");
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void internalFrameIconified(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void internalFrameOpened(InternalFrameEvent arg0) {
		openedFrames++;
	}

}
