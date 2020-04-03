package de.unileipzig.irpact.testdata.def1;

/// description: Data-Tiefe 2
/// type: String
/// identifier: Data-Tiefe 2
/// unit:
/// domain:
/// validation:
/// hidden:
/// processing:
public class Data2 {

    public String $name;

    /// description:
    /// type: float
    /// identifier:
    /// unit:
    /// domain:
    /// validation:
    /// hidden:
    /// processing:
    public double value2;

    /// description: Zugehörigkeit zu Data 3
    /// identifier: Zugehörigkeit zu Data 3
    public Data3[] data3s;

    /// description: Zugehörigkeit zu OtherData 2
    /// identifier: Zugehörigkeit zu OtherData 2
    public OtherData2[] otherData2s;

    // end_of_definition
}
