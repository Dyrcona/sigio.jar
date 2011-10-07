/*
 * Copyright © 2011 Jason J.A. Stephenson
 * 
 * This file is part of sigio.jar.
 * 
 * sigio.jar is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * sigio.jar is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 * 
 * You should have received a copy of the Lesser GNU General Public License
 * along with sigio.jar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sigio.sql;
import javax.swing.table.AbstractTableModel;
import java.util.Vector;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

/**
 * A class to implement a Swing AbstractTableModel that can be used to
 * display a ResultSet in a JTable.
 */
public class ResultSetTableModel extends AbstractTableModel {

	private Vector<Vector<Object>> rows = null;
	private ResultSetMetaData metaData = null;

	/**
	 * Simple constructor.
	 */
	public ResultSetTableModel() {
		super();
	}

	/**
	 * Construct a ResultSetTableModel with an existing ResultSet.
	 */
	public ResultSetTableModel(ResultSet rs) throws SQLException {
		super();
		this.setResultSet(rs);
	}

	/**
	 * Set or change the result set used for the model.
	 *
	 * @param rs new resultset to use
	 */
	public void setResultSet(ResultSet rs) throws SQLException {
		this.metaData = rs.getMetaData();
		this.rows = new Vector<Vector<Object>>();
		while (rs.next()) {
			Vector<Object> row = new Vector<Object>();
			int i;
			for (i = 1; i <= this.getColumnCount(); i++) {
				Object value;
				value = rs.getObject(i);
				row.addElement(value);
			}
			this.rows.addElement(row);
		}
		this.fireTableChanged(null);
	}

	/**
	 * Get the object class represented by a table model column.
	 *
	 * @param column int number of the column to access; columns are
	 * numbered beginning with 0
	 * @return class of the object stored in the requested column
	 */
	public Class getColumnClass(int column) {
		String className;
		int type;

		try {
			type = this.metaData.getColumnType(column + 1);
		}
		catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
			System.err.println("SQLState: " + e.getSQLState());
			System.err.println("VendorError: " + e.getErrorCode());

			return super.getColumnClass(column);
		}

		switch (type) {
		case Types.BIT:
			className = "java.lang.Boolean";
			break;
		case Types.TINYINT:
			className = "java.lang.Byte";
			break;
		case Types.SMALLINT:
			className = "java.lang.Short";
			break;
		case Types.INTEGER:
			className = "java.lang.Integer";
			break;
		case Types.BIGINT:
			className = "java.lang.Long";
			break;
		case Types.REAL:
			className = "java.lang.Float";
			break;
		case Types.FLOAT:
		case Types.DOUBLE:
			className = "java.lang.Double";
			break;
		case Types.DECIMAL:
		case Types.NUMERIC:
			className = "java.math.BigDecimal";
			break;
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
			className = "java.lang.String";
			break;
		case Types.DATE:
			className = "java.sql.Date";
			break;
		case Types.TIME:
			className = "java.sql.Time";
			break;
		case Types.TIMESTAMP:
			className = "java.sql.TimeStamp";
			break;
		case Types.BINARY:
		case Types.VARBINARY:
		case Types.LONGVARBINARY:
			className = "byte[]";
			break;
		case Types.BLOB:
			className = "java.sql.Blob";
			break;
		case Types.CLOB:
			className = "java.sql.Clob";
			break;
		case Types.ARRAY:
			className = "java.sql.Array";
			break;
		case Types.STRUCT:
			className = "java.sql.Struct";
			break;
		case Types.OTHER:
		case Types.JAVA_OBJECT:
			className = "java.lang.Object";
			break;
		default:
			return super.getColumnClass(column);
		}

		try {
			return Class.forName(className);
		}
		catch (Exception e) {
			e.printStackTrace();
			return super.getColumnClass(column);
		}
	}

	/**
	 * Get the number of columns in the model.
	 */
	public int getColumnCount() {
		int count = 0;

		try {
			if (this.metaData != null)
				count = this.metaData.getColumnCount();
		}
		catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
			System.err.println("SQLState: " + e.getSQLState());
			System.err.println("VendorError: " + e.getErrorCode());

			count = 0;
		}

		return count;
	}

	/**
	 * Get the name of a given column.
	 *
	 * @param column int index of the column
	 * @return string name of the column at the index
	 */
	public String getColumnName(int column) {
		String columnName = " ";

		try {
			if (this.metaData != null)
				columnName = this.metaData.getColumnName(column + 1);
		}
		catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
			System.err.println("SQLState: " + e.getSQLState());
			System.err.println("VendorError: " + e.getErrorCode());

			columnName = " ";
		}

		return columnName;
	}

	/**
	 * Get the number of rows in the model.
	 */
	public int getRowCount() {
		int rowCount = 0;
		if (this.rows != null)
			rowCount = this.rows.size();
		return rowCount;
	}

	/**
	 * Check if a given cell is editable.
	 *
	 * @param row int index of the cell row
	 * @param column int index of the cell column
	 * @return <code>true</code> if the cell at row/column index can
	 * be edited; <code>false</code> otherwise
	 */
	public boolean isCellEditable(int row, int column) {
		boolean isEditable = false;

		try {
			if (this.metaData != null) 
				isEditable = this.metaData.isWritable(column + 1);
		}
		catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
			System.err.println("SQLState: " + e.getSQLState());
			System.err.println("VendorError: " + e.getErrorCode());

			isEditable = false;
		}

		return isEditable;
	}

	/**
	 * Get the object stored in a given cell.
	 *
	 * @param row int index of the cell row
	 * @param column int index of the cell column
	 * @return object stored in the given cell
	 */
	public Object getValueAt(int row, int column) {
		if (row < 0 || row > this.rows.size() ||
			column < 0 || column > this.getColumnCount()) {
			return null;
		}

		Vector r = (Vector) this.rows.elementAt(row);
		Object value = r.elementAt(column);
		if (value instanceof byte[]) {
			byte[] byteValue = (byte[]) value;
			value = "byte[" + byteValue.length + "]";
		}
		return value;
	}

	/**
	 * Set the object stored in a given cell.
	 *
	 * @param row int index of the cell row
	 * @param column int index of the cell column
	 */
	public void setValueAt(Object value, int row, int column) {
		if (row < 0 || row > this.rows.size() ||
			column < 0 || column > this.getColumnCount()) {
			return;
		}

		Vector<Object> r =  this.rows.elementAt(row);
		r.setElementAt(value, column);
	}
}
