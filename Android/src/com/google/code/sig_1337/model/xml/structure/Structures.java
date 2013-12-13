package com.google.code.sig_1337.model.xml.structure;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.code.sig_1337.model.xml.ITriangle;
import com.google.code.sig_1337.model.xml.ITriangles;
import com.google.code.sig_1337.model.xml.TrianglesType;

public class Structures<T extends IStructure> implements IStructures<T> {

	/**
	 * Inner list.
	 */
	private final List<T> inner;

	/**
	 * If the structures have been loaded.
	 */
	private boolean loaded;

	/**
	 * Total number of vertex.
	 */
	private int vertexCount;

	/**
	 * Total number of colors.
	 */
	private int colorCount;

	/**
	 * Total number of index.
	 */
	private int indexCount;

	/**
	 * Vertex buffer.
	 */
	private FloatBuffer vertexBuffer;

	/**
	 * Color buffer.
	 */
	private FloatBuffer colorBuffer;

	/**
	 * Default constructor.
	 */
	public Structures() {
		super();
		inner = new ArrayList<T>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(T structure) {
		inner.add(structure);
		int s = 0;
		for (ITriangles triangles : structure.getTriangles()) {
			s += triangles.size();
		}
		vertexCount += s * 9;
		colorCount += s * 4 * 3;
		indexCount += s * 3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		inner.clear();
		loaded = false;
		vertexCount = 0;
		colorCount = 0;
		indexCount = 0;
		if (vertexBuffer != null) {
			vertexBuffer.clear();
			vertexBuffer = null;
		}
		if (colorBuffer != null) {
			colorBuffer.clear();
			colorBuffer = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return inner.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return inner.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void done() {
		// Create the buffer, vertexCount * sizeof(float).
		ByteBuffer bb = ByteBuffer.allocateDirect(vertexCount * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		// Create the buffer, colorCount * sizeof(float).
		bb = ByteBuffer.allocateDirect(colorCount * 4);
		bb.order(ByteOrder.nativeOrder());
		colorBuffer = bb.asFloatBuffer();
		// For all the structures.
		for (T structure : inner) {
			StructureType structureType = structure.getType();
			// For all the lists of triangles.
			for (ITriangles triangles : structure.getTriangles()) {
				TrianglesType type = triangles.getType();
				// For all the triangles.
				for (ITriangle triangle : triangles) {
					triangle.fill(vertexBuffer);
					type.fill(colorBuffer, structureType);
				}
			}
		}
		// Reset the position.
		vertexBuffer.position(0);
		colorBuffer.position(0);
		// Set loaded to true.
		loaded = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FloatBuffer getVertexBuffer() {
		return vertexBuffer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FloatBuffer getColorBuffer() {
		return colorBuffer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getIndexCount() {
		return indexCount;
	}

}
