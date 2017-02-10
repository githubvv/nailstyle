package controllers.admin;

import beans.PageNavigator;
import javax.faces.context.FacesContext;
import java.util.List;

public abstract class AbstractModeController<T> {
	private T selectedObject;
	private boolean groupChange = false;
	private boolean editModeView = false;
	private boolean addModeView = false;
	private boolean removeModeView = false;
	private boolean confirmModeView = false;

/* GETTERS - SETTERS */
	public boolean isGroupChange() {
		return groupChange;
	}

	public void setGroupChange(boolean groupChange) {
		this.groupChange = groupChange;
	}

	public boolean isEditModeView() {
		return editModeView;
	}

	public void setEditModeView(boolean editModeView) {
		this.editModeView = editModeView;
	}

	public boolean isAddModeView() {
		return addModeView;
	}

	public void setAddModeView(boolean addModeView) {
		this.addModeView = addModeView;
	}

	public boolean isRemoveModeView() {
		return removeModeView;
	}

	public void setRemoveModeView(boolean removeModeView) {
		this.removeModeView = removeModeView;
	}

	public boolean isConfirmModeView() {
		return confirmModeView;
	}

	public void setConfirmModeView(boolean confirmModeView) {
		this.confirmModeView = confirmModeView;
	}

	public T getSelectedObject() {
		return selectedObject;
	}

	public void setSelectedObject(T selectedObject) {
		this.selectedObject = selectedObject;
	}

/* BUSINESS LOGIC */
	public void switchConfirmMode(boolean value) {
		confirmModeView = value;
	}

	public void switchAddMode() {
		addModeView = true;
		setNewObjForSelectedObj();
		editModeView = false;
		removeModeView = false;
	}

    protected abstract void setNewObjForSelectedObj();

    public void switchEditMode() {
		editModeView = true;
		addModeView = false;
		removeModeView = false;
	}

	public void switchRemoveMode() {
		setConfirmModeView(true);
		removeModeView = true;
		editModeView = false;
		addModeView = false;
	}

	public void cancelModes() {
		editModeView = false;
		addModeView = false;
		removeModeView = false;
		confirmModeView = false;
	}

}
