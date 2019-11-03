package army.org.dmbtimer.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import army.org.dmbtimer.R
import army.org.dmbtimer.fragments.HomeFragment
import army.org.dmbtimer.menu.MenuConst.*
import com.etebarian.meowbottomnavigation.MeowBottomNavigation

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initMenu(findViewById(R.id.navigation_bar))
        val fm: FragmentManager = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.add(R.id.fragment, HomeFragment())
        ft.commit()
    }

    private fun initMenu(meowBottomNavigation: MeowBottomNavigation) {
        meowBottomNavigation.add(MeowBottomNavigation.Model(HOME.ordinal, R.drawable.ic_home))
        meowBottomNavigation.add(MeowBottomNavigation.Model(CALENDAR.ordinal, R.drawable.ic_calendar))
        meowBottomNavigation.add(MeowBottomNavigation.Model(EVENT.ordinal, R.drawable.ic_event))
        meowBottomNavigation.add(MeowBottomNavigation.Model(PROFILE.ordinal, R.drawable.ic_profile))
        meowBottomNavigation.add(MeowBottomNavigation.Model(SETTINGS.ordinal, R.drawable.ic_settings))

        meowBottomNavigation.show(HOME.ordinal, false)

        meowBottomNavigation.setOnClickMenuListener {
            when (it.id) {
                HOME.ordinal -> {
                }
                CALENDAR.ordinal -> {
                }
                EVENT.ordinal -> {
                }
                PROFILE.ordinal -> {
                }
                SETTINGS.ordinal -> {
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
