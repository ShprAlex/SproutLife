/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.renderer.colors;

import java.awt.Color;

import com.sproutlife.model.echosystem.Organism;

public interface ColorModel {
	public Color getCellColor(Organism o);

	public Color getHeadColor(Organism o);

	public Color getTailColor(Organism o);

	public Color getGenomeBackgroundColor(Organism o);
}
