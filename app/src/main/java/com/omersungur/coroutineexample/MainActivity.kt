package com.omersungur.coroutineexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var userName = ""
        var userAge = 0

        //runBlocking, blocklayarak scope oluşturur ve içindeki işlem bitene kadar diğer kodları bloklar.
        //Global scope, bütün uygulamada kullanılabilecek bir kapsamda açılır.
        //coroutineScope'u çalıştırabilmek için bir coroutine içinde veya suspend fonksiyon kullanmalıyız.
        //CoroutineScope'u context verip direkt çalıştırabiliriz. (coroutineScope CoroutineScope aynı değiller.)
        //Dispatcher ile hangi threadde işlem yapacağımızı seçebiliriz.
        //Job ile coroutine işlemimiz bittikten sonra ne yapacağımza karar verebileceğimiz yapılar kurabiliriz.
        //withContext kullanırsak, içinde olduğumuz threadden farklı bir threadde işlem yapabilme imkanımız olur.

        //Coroutinlerle try catch yapısı kullanmak mantıklı değildir! Coroutine Exception Handling yapısını kullanmamız gerekiyor.
        //Coroutine Exception Handling kullandık diyelim ve scope içinde bir launch hata verirse diğer launchlar hata vermese bile çalışmaz.
        //Bu sebepten ötürü supervisorScope'u kullanmamız gerekir ve kaç tane launch hata verirse versin çalışan launch varsa onu çalıştırır.
        //lifecycleScope içinde direkt coroutinelerimizi kullanabiliriz(runBlock, GlobalScope ve coroutineScope'a gerek yok).

        /*
        GlobalScope.launch { // Bu işlemi threadlerle yapsak belki çökebilirdi.
             repeat(100000) {
                 launch {
                     println("Android")
                 }
             }
         }*/

        /*
        println("Run Blocking starts.")
        runBlocking {
            launch {
                delay(5000)
                println("Run Blocking is running.")
            }
        }
        println("Run Blocking finishes")*/

        /*
        CoroutineScope(Dispatchers.Default).launch {
            //Dispatchers.Default -> CPU Intensive
            //Dispatchers.IO -> Input / Output, Networking
            //Dispatchers.Main -> UI
            //Dispatchers.Unconfined -> Inherited dispatcher
            delay(2000)
            print("CoroutineScope is running")
        }
        runBlocking {

            launch(Dispatchers.Main) {
                println("Main Thread: ${Thread.currentThread().name}")
            }
        }*/

        // myFunction() coroutine scope'u içinden çağırabiliriz.

        /*GlobalScope.launch(Dispatchers.Default) {

            val downloadedName = async {
                downloadName()
            }

            val downloadedAge = async {
                downloadAge()
            }

            userName = downloadedName.await() // await ile işlemin bitmesini bekliyoruz.
            userAge = downloadedAge.await() // buradaki işlemler bitmeden aşağıdaki değeri yazdıramayız o yüzden bura bittikten sonra ora çalışacak.

            println(userName + " " +  userAge )
        }*/

        /*GlobalScope.launch {

            val myJob = launch {
                delay(2000)
                println("job")
                val secondJob = launch {
                    delay(2000)
                    println("job 2 ")
                }
            }

            myJob.invokeOnCompletion {// Job bittiğinde yapılacak işleri  burada yazabiliriz.
                println("Jobs are done.")
            }

            //myJob.cancel() job'u direkt iptal eder (En üstteki job iptal edilince içindeki bütün joblarda iptal edilir).
        }*/

        /*runBlocking {
            launch(Dispatchers.Default) {
                println("Default threadde yapılacak coroutine")
                withContext(Dispatchers.IO) {
                    println("IO threadde yapılacak coroutine")
                }
            }
        }*/

        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
            println(throwable.localizedMessage)
        }

        lifecycleScope.launch(handler){
            supervisorScope {
                launch {
                    throw Exception("exception")
                    /*launch {
                        println("Bu bloğun üstünde exception olduğu için bu bloğa hiç ulaşamayız.")
                    }*/
                }
                launch {
                    println("Üstteki launch hata fırlatıyor dahi olsa burası çalışacak çünkü supervisorScope içindeyiz!")
                }
            }
        }

    }


    suspend fun myFunction() {
        coroutineScope {
            launch {
                println("Suspend Function")
            }
        }
    }

    suspend fun downloadName() : String {
        delay(2000)
        val userName = "Omer"
        println("userName downloaded")
        return userName
    }

    suspend fun downloadAge() : Int {
        delay(4000)
        val userAge = 30
        println("userAge downloaded")
        return userAge
    }
}