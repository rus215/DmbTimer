package army.org.dmbtimer.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import army.org.dmbtimer.R
import army.org.dmbtimer.fragments.CalendarFragment
import army.org.dmbtimer.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Инициализация меню
        initMenu()

        //Загружаем home фрагмент
        val fm: FragmentManager = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.add(R.id.fragment, HomeFragment())
        ft.commit()
    }

    private fun initMenu() {


        navigationBar.setOnNavigationItemSelectedListener {
            var currFragment: Fragment = HomeFragment()

            when (it.itemId) {
                R.id.calendar -> {
                    currFragment = CalendarFragment()
                }
                R.id.events -> {
                    currFragment = CalendarFragment()
                }
                R.id.profile -> {
                    currFragment = CalendarFragment()
                }
                R.id.settings -> {
                    currFragment = CalendarFragment()
                }
            }
            val fm: FragmentManager = supportFragmentManager
            val ft = fm.beginTransaction()
            ft.replace(R.id.fragment, currFragment)
            ft.commit()

            return@setOnNavigationItemSelectedListener true
        }

        navigationBar.setOnNavigationItemReselectedListener {
            //Заглушка на повторный выбор элемента меню
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
