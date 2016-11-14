package ru.terra.universal.shared.entity;

public abstract class EntityCoordinates {
	protected float x = 0f;
	protected float y = 0f;
	protected float z = 0f;
	protected float h = 0f;

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getH() {
		return h;
	}

	public void setH(float h) {
		this.h = h;
	}

	public void applyCoordinates(EntityCoordinates ec) {
		setH(ec.getH());
		setX(ec.getX());
		setY(ec.getY());
		setZ(ec.getZ());
	}
}
