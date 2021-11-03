package util;

import java.util.ArrayList;

import processing.core.PApplet;

public class Grid<T> {
	final T empty = (T) new Object();

	private ArrayList<ArrayList<T>> val;
	private int nx, px, ny, py;

	public Grid(int nx, int px, int ny, int py) {
		this.nx = nx;
		this.px = px;
		this.ny = ny;
		this.py = py;
		this.val = new ArrayList<ArrayList<T>>(this.nx + this.px);
		for (int x = 0; x < this.nx + this.px; x++) {
			this.val.add(new ArrayList<T>());
			for (int y = 0; y < this.ny + this.py; y++) {
				this.val.get(x).add(this.empty);
			}
		}
	}

	public Grid(int x, int y) {
		this(x, x, y, y);
	}

	public Grid(int s) {
		this(s, s, s, s);
	}

	public void add(int x, int y, T v) {
		this.expand(x, y);
		this.val.get(x + this.nx).set(y + this.ny, v);
	}

	public void set(int x, int y, T v) throws GridIndexOutOfBoundsException {
		try {
			this.indexExcept(x, y);
		} catch (GridIndexOutOfBoundsException e) {
			throw new GridIndexOutOfBoundsException();
		}
		this.val.get(x + this.nx).set(y + this.ny, v);
	}

	public T get(int x, int y) throws GridIndexOutOfBoundsException {
		this.ensure();
		try {
			this.indexExcept(x, y);
		} catch (GridIndexOutOfBoundsException e) {
			throw new GridIndexOutOfBoundsException(e);
		}
		return this.val.get(x + this.nx).get(y + this.ny);
	}

	/*
	 * makes val is large enough to contain a variable at at least (x, y)
	 */
	public void expand(int x, int y) {
		if (x > 0 && x > this.px) {
			this.px = x + 1;
		} else if (x < 0 && x < -this.nx) {
			this.nx = -x;
		}
		if (y > 0 && y > this.py) {
			this.py = y + 1;
		} else if (y < 0 && y < -this.ny) {
			this.ny = -y;
		}
		if (this.val.size() < x + this.nx || this.val.get(x + this.nx) == null) {
			this.val.add(x + this.nx, new ArrayList<T>(this.ny + this.py));
		}
		if (this.val.get(x + this.nx).size() < y + this.ny || this.val.get(x + this.nx).get(y + this.ny) == null) {
			this.val.get(x + this.nx).add(y + this.ny, this.empty);
		}
	}

	/*
	 * makes sure dimensions for val match the nx, px, ny, and py
	 */
	public void ensure() {
		for (int x = 0; x < this.nx + this.px; x++) {
			if (x >= this.val.size() || this.val.get(x) == null) {
				this.val.add(x, new ArrayList<T>());
			}
			for (int y = 0; y < this.ny + this.py; y++) {
				if (y >= this.val.get(x).size() || this.val.get(x).get(y) == null) {
					this.val.get(x).add(y, this.empty);
				}
			}
		}
	}

	public void ensure(int x, int y) {
		try {
			this.indexExcept(x, y);
		} catch (GridIndexOutOfBoundsException e) {
			throw new GridIndexOutOfBoundsException();
		}
		for (int i = this.val.get(x + this.nx).size(); i < this.ny + this.py; i++) {
			this.val.get(x + this.nx).set(i, this.empty);
		}
		if (this.val.size() < x + this.nx || this.val.get(x + this.nx) == null) {
			this.val.add(x + this.nx, new ArrayList<T>(this.ny + this.py));
		}
		if (this.val.get(x + this.nx).size() < y + this.ny || this.val.get(x + this.nx).get(y + this.ny) == null) {
			this.val.get(x + this.nx).add(y + this.ny, this.empty);
		}
	}

	public void indexExcept(int x, int y) {
		if (x >= this.px || x < -this.nx || y >= this.py || y < -this.ny) {
			throw new GridIndexOutOfBoundsException(" g: (" + x + ", " + y + ")");
		}
	}

	public int[] size(boolean includeNegative) {
		if (!includeNegative) {
			return new int[] { this.nx + this.px, this.ny + this.py };
		} else {
			return new int[] { this.nx, this.px, this.ny, this.py };
		}
	}

	/*
	 * neg x, pos x, neg y, pos y
	 */
	public int[] size() {
		return this.size(true);
	}

	public String toString() {
		String out = "";
		this.ensure();
		out += "" + this.val.size() + " ?= " + (this.nx + this.px) + PApplet.ENTER;
		for (ArrayList<T> v : this.val) {
			out += "" + v.size() + " ?= " + (this.ny + this.py) + PApplet.TAB;
		}
		return out;
	}
}

class GridIndexOutOfBoundsException extends IndexOutOfBoundsException {
	GridIndexOutOfBoundsException(String text) {
		super(text);
	}

	GridIndexOutOfBoundsException() {
		super();
	}

	GridIndexOutOfBoundsException(GridIndexOutOfBoundsException e) {
		super(e.getMessage());
	}
}
