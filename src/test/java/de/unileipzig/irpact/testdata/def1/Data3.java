package de.unileipzig.irpact.testdata.def1;

/// description: Data-Tiefe 3
/// type: String
/// identifier: Data-Tiefe 3
/// unit:
/// domain:
/// validation:
/// hidden:
/// processing:
public class Data3 {

    public String $name;

    /// description:
    /// type: float
    /// identifier:
    /// unit:
    /// domain:
    /// validation:
    /// hidden:
    /// processing:
    public double value3;

    /// description: Zugehörigkeit zu Data 4
    /// identifier: Zugehörigkeit zu Data 4
    public Data4[] data4s;

    /// description: Zugehörigkeit zu OtherData 3
    /// identifier: Zugehörigkeit zu OtherData 3
    public OtherData3[] otherData3s;

    // end_of_definition
}
