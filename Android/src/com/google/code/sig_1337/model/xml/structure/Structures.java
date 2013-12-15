package com.google.code.sig_1337.model.xml.structure;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.code.sig_1337.model.xml.ITriangle;
import com.google.code.sig_1337.model.xml.ITriangles;
import com.google.code.sig_1337.model.xml.TrianglesType;

public class Structures<T extends IStructure> implements IStructures<T> {

	/**
	 * Inner list.
	 */
	private final List<T> inner;

	/**
	 * Structures by name.
	 */
	private final Map<String, T> map;

	/**
	 * Type.
	 */
	private final StructureType type;

	/**
	 * If the structures have been loaded.
	 */
	private boolean loaded;

	/**
	 * Total number of vertex.
	 */
	private int filledVertexCount;

	/**
	 * Total number of vertex.
	 */
	private int holeVertexCount;

	/**
	 * Total number of index.
	 */
	private int filledIndexCount;

	/**
	 * Total number of index.
	 */
	private int holeIndexCount;

	/**
	 * Vertex buffer.
	 */
	private FloatBuffer filledVertexBuffer;

	/**
	 * Vertex buffer.
	 */
	private FloatBuffer holeVertexBuffer;

	/**
	 * Default constructor.
	 */
	public Structures(StructureType type) {
		super();
		inner = new ArrayList<T>();
		map = new HashMap<String, T>();
		this.type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StructureType getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(T structure) {
		inner.add(structure);
		map.put(structure.getName(), structure);
		for (ITriangles triangles : structure.getTriangles()) {
			int s = triangles.size();
			switch (triangles.getType()) {
			case Filled:
				filledVertexCount += s * 9;
				filledIndexCount += s * 3;
				break;
			case Hole:
				holeVertexCount += s * 9;
				holeIndexCount += s * 3;
				break;
			default:
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T get(String name) {
		if (name == null) {
			return null;
		} else {
			return map.get(name);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		inner.clear();
		map.clear();
		loaded = false;
		filledVertexCount = 0;
		holeVertexCount = 0;
		filledIndexCount = 0;
		holeIndexCount = 0;
		if (filledVertexBuffer != null) {
			filledVertexBuffer.clear();
			filledVertexBuffer = null;
		}
		if (holeVertexBuffer != null) {
			holeVertexBuffer.clear();
			holeVertexBuffer = null;
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
		ByteBuffer bb = ByteBuffer.allocateDirect(filledVertexCount * 4);
		bb.order(ByteOrder.nativeOrder());
		filledVertexBuffer = bb.asFloatBuffer();
		// Create the buffer, vertexCount * sizeof(float).
		bb = ByteBuffer.allocateDirect(holeVertexCount * 4);
		bb.order(ByteOrder.nativeOrder());
		holeVertexBuffer = bb.asFloatBuffer();
		// For all the structures.
		for (T structure : inner) {
			// For all the lists of triangles.
			for (ITriangles triangles : structure.getTriangles()) {
				TrianglesType type = triangles.getType();
				// For all the triangles.
				for (ITriangle triangle : triangles) {
					switch (type) {
					case Filled:
						triangle.fill(filledVertexBuffer);
						break;
					case Hole:
						triangle.fill(holeVertexBuffer);
						break;
					default:
					}
				}
			}
		}
		// Reset the position.
		filledVertexBuffer.position(0);
		holeVertexBuffer.position(0);
		// Set loaded to true.
		loaded = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FloatBuffer getFilledVertexBuffer() {
		return filledVertexBuffer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FloatBuffer getHoleVertexBuffer() {
		return holeVertexBuffer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getFilledIndexCount() {
		return filledIndexCount;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHoleIndexCount() {
		return holeIndexCount;
	}

}
