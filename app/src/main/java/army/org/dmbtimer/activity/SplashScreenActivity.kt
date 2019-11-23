package army.org.dmbtimer.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import army.org.dmbtimer.room.database.SoldierViewModel

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val soldierViewModel: SoldierViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory(application)
            ).get(SoldierViewModel::class.java)

        soldierViewModel.allSoldiers.observe(this, Observer {
            if (it.isEmpty()) {
                //Если нет запускаем окно добавление солдата
                val intent = Intent(this, AddActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        })
    }

}
