package de.unileipzig.irpact.testdata.def1;

/// description: Data-Tiefe 1
/// type: String
/// identifier: Data-Tiefe 1
/// unit:
/// domain:
/// validation:
/// hidden:
/// processing:
public class Data1 {

    public String $name;

    /// description:
    /// type: float
    /// identifier:
    /// unit:
    /// domain:
    /// validation:
    /// hidden:
    /// processing:
    public double value1;

    /// description: Zugehörigkeit zu Data 2
    /// identifier: Zugehörigkeit zu Data 2
    public Data2[] data2s;

    // end_of_definition
}
