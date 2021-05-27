package com.example.reckeyboard.Database

class Row {
    var id : Int = 0
    var character : String = ""
    var count : Int = 1
    var holdTime : Int = 0
    var pressure : Float = 0.0f

    constructor(character: String, count: Int, holdTime: Int, pressure: Float){
        this.character= character
        this.count = count
        this.holdTime = holdTime
        this.pressure = pressure
    }
    constructor(){}
}
