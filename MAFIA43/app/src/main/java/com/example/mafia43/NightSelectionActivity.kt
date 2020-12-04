package com.example.mafia43

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable

class NightSelectionActivity : AppCompatActivity() {

    private lateinit var mConfirmButton : Button
    private lateinit var mPlayers : Array<Player>
    private lateinit var currPlayers : Array<Player>
    private lateinit var mBundle : Bundle
    private lateinit var selected : String
    private lateinit var mRoleView : TextView
    private lateinit var mPromptView : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.night_role_selection)

        /* Get players array from intent Bundle */
        mBundle = intent.getBundleExtra("Bundle")!!
        mPlayers = mBundle.getSerializable("playersArr") as Array<Player>
        val role = intent.getIntExtra("Role", 0)
        val alive = intent.getIntExtra("AlivePlayers", mPlayers.size)
        mRoleView = findViewById(R.id.nsTextView2)
        mPromptView = findViewById(R.id.nsTextView3)

        when(role) {
            MAFIA -> {
                mRoleView.setText("MAFIA")
                mPromptView.setText("Who do you want to kill?")
                currPlayers = Array<Player>(alive - 1) {Player("", 0)}
                var j = 0
                for(i in 0..mPlayers.size-1) {
                    if (mPlayers[i].alive() && mPlayers[i].role() != MAFIA) {
                        currPlayers[j] = mPlayers[i]
                        j++
                    }
                }
            }
            DOCTOR -> {
                mRoleView.setText("DOCTOR")
                mPromptView.setText("Who do you want to save?")
                currPlayers = Array<Player>(alive) {Player("", 0)}
                var j = 0
                for(i in 0..mPlayers.size-1) {
                    if (mPlayers[i].alive()) {
                        currPlayers[j] = mPlayers[i]
                        j++
                    }
                }
            }
            DETECTIVE -> {
                mRoleView.setText("DETECTIVE")
                mPromptView.setText("Who do you want to check?")
                currPlayers = Array<Player>(alive-1) {Player("", 0)}
                var j = 0
                for(i in 0..mPlayers.size-1) {
                    if (mPlayers[i].alive() && mPlayers[i].role() != DETECTIVE) {
                        currPlayers[j] = mPlayers[i]
                        j++
                    }
                }
            }
        }

        /* Instantiate listView and Adapter */


        /* If you want to recreate this, just create a ListView in the layout resource file,
         and use your listView instead of "player_list" */

        val listView = findViewById<ListView>(R.id.player_list)
        listView.adapter = PlayerListAdapter(
            this,
            R.layout.player_list_item,
            currPlayers
        )

        listView.setBackgroundColor(resources.getColor(R.color.white, null))
        selected = ""

        /* position is items position in current players array */
        listView.setOnItemClickListener{ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            Log.i(TAG, currPlayers[position].name())
            selected = currPlayers[position].name()
        }

        /* When button is clicked */
        mConfirmButton = findViewById(R.id.confirm_button)

        mConfirmButton.setOnClickListener{
            if (selected == "") {
                mPromptView.setText(mPromptView.text.toString() + "\nSelect a player...")
            } else {
                when (role) {
                    MAFIA -> {
                        val nightIntent =
                            Intent(this@NightSelectionActivity, NightActivity::class.java)
                        /* You have to create a Bundle to pass the Player array */
                        val args = Bundle()
                        args.putSerializable("playersArr", mPlayers as Serializable)
                        nightIntent.putExtra("Bundle", args)
                        nightIntent.putExtra("AlivePlayers", alive)
                        nightIntent.putExtra("Role", DOCTOR)
                        nightIntent.putExtra("Kill", selected)
                        nightIntent.putExtra("Save", "")
                        startActivity(nightIntent)
                    }
                    DOCTOR -> {
                        val nightIntent =
                            Intent(this@NightSelectionActivity, NightActivity::class.java)
                        /* You have to create a Bundle to pass the Player array */
                        val args = Bundle()
                        args.putSerializable("playersArr", mPlayers as Serializable)
                        nightIntent.putExtra("Bundle", args)
                        nightIntent.putExtra("AlivePlayers", alive)
                        nightIntent.putExtra("Role", DETECTIVE)
                        nightIntent.putExtra("Kill", intent.getStringExtra("Kill"))
                        nightIntent.putExtra("Save", selected)
                        startActivity(nightIntent)
                    }
                    DETECTIVE -> {
                        val nightIntent =
                            Intent(this@NightSelectionActivity, DetectiveActivity::class.java)
                        /* You have to create a Bundle to pass the Player array */
                        val args = Bundle()
                        args.putSerializable("playersArr", mPlayers as Serializable)
                        nightIntent.putExtra("Bundle", args)
                        nightIntent.putExtra("AlivePlayers", alive)
                        nightIntent.putExtra("Kill", intent.getStringExtra("Kill"))
                        nightIntent.putExtra("Save", intent.getStringExtra("Save"))
                        nightIntent.putExtra("Check", selected)
                        startActivity(nightIntent)
                    }
                }
            }
        }
    }

    companion object{
        const val TAG = "night_selection"
        const val MAFIA = 1
        const val DOCTOR = 2
        const val DETECTIVE = 3
        const val CIVILIAN = 4
    }
}