package ui.anwesome.com.kotlinplusfarview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.plusfarview.PlusFarView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlusFarView.create(this)
    }
}
