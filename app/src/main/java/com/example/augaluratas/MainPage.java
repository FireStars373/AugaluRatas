package com.example.augaluratas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class MainPage extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton sidebar = findViewById(R.id.sidebar_from_main_page);
        TextView username = findViewById(R.id.main_page_username);


        SharedPreferences sharedPref = getBaseContext().getSharedPreferences("augalu_ratas.CURRENT_USER_KEY", Context.MODE_PRIVATE);
        Long current_id = sharedPref.getLong("current_user_id", 0);
        Users user = AppActivity.getUser_PostDatabase().usersDAO().getUserById(current_id);
        username.setText(user.getUsername());
        NewsDao newsDao = AppActivity.getNewsDatabase().newsDao();
        //Deleting example data
        //newsDao.deleteAll();


        if (newsDao.getAllNews().isEmpty()){
            //Inserting example data if empty
            newsDao.insert(new News(
                    "8 mažai priežiūros reikalaujantys kambariniai augalai: juos itin sunku numarinti",
                    "Jei jums sunkiai sekasi tinkamai puoselėti augalus, tačiau vis tiek norite jų turėti savo namuose, yra išeitis. Pasirodo, kai kurie augalai beveik nereikalauja priežiūros, o tai reiškia, kad jų beveik neįmanoma numarinti.",
                    "Kambariniai augalai paprastai namams suteikia grožio, spalvų ir gyvybiškumo, tačiau ne visada lengva jais pasirūpinti. Viena dažniausiai kylančių problemų – nepakankamas laistymas arba, priešingai, perlaistymas, rašoma puslapyje „Better Homes&Gardens“. Visgi dalis kambarinių augalų nereikalauja daug priežiūros ir yra itin atlaidūs – jų kone neįmanoma numarinti.\n"  +"\n" + "Vėzdūnė Vienas iš populiarių pasirinkimų patalpoms – vėzdūnė, kuri pakenčia silpną apšvietimą, mažą drėgmę ir nepastovų laistymą. Žydi dažniausiai vasarą, bet gali pražysti bet kuriuo metų laiku.\n"  +"\n" +"Trijuostė sansevjera Šis augalas gerai auga beveik bet kurioje patalpoje. Lapai paprastai būna pilkai žalios spalvos, taip pat gali būti su geltonais ar baltais apvadais. Gali augti silpnoje šviesoje, tačiau geriau sekasi vidutinėje arba ryškioje šviesoje.",
                    convertDrawableToByteArray(R.drawable.news1)));
            newsDao.insert(new News(
                    "Paragavęs namuose augančio augalo, katinas Jeronimas patyrė tikrą košmarą: sveikata sutriko vos nuo kelių lapų",
                    "Vilnietė Asta prismena išgąstį, kai vieną dieną jos kruopščiai iš duonvaisio sėklos išauginti daigeliai netikėtai prarado lapus. Įtarimai greitai krito ant garbaus amžiaus šeimos numylėtinio – aštuoniolikmečio  Jeronimo, kuris, kaip pasakoja ji, neatsispiria žaliuojančiai augmenijai. Neilgai trukus katinas ėmė negaluoti, o šiandien moteris džiaugiasi, kad viskas baigėsi laimingai, ir ragina kačių šeimininkus atidžiai rinktis namuose auginamus augalus, jog netektų stebėti augintinio kančių.",
                    "Asta pasakoja sumaniusi iš duonvaisio sėklų išauginti augalus, mat vaisius egzotinis ir įdomus, todėl ir daiginimo procesas jai pasirodė vertas dėmesio. Tada ji nesusimąstė, kad tai gali tapti didžiule problema. Sėklos sėkmingai išleido daigelius, tačiau trys daigeliai įtartinai augo nevienodai – vienas daigas greitai apsirėdyjo lapais, kiti – liko stagarais. Vis dėlto, dabar moteris svarsto, kad taip nutiko ne veltui – lapuotas daigas stovėjo atokioje, katinui nepasiekiamoje vietoje, kiti du – atviroje ir katinui prieinamoje teritorijoje.\n" + "\n" + "„Tik Jeronimui pradėjus negaluoti ir radus lapą ant grindų supratau, kad tuos du augalus galėjo nukramtyti jis. Iki šiol to nepastebėjau – maniau, kad jie tiesiog prastai auga, gal netiko vieta ar tiesiog nebuvo lemta“, – pasakoja ji.\n" + "\n" + "Asta prisimena, kad Jeronimas 2,5 valandos neatsitraukdamas lakė vandenį iš tekančio krano, o viduriai buvo užkietėję taip, kad katinas tuštinosi net klykdamas. Laimei, jo būklė po kelių valandų pagerėjo, o katinas dabar sveikas, kaip iki šiol. \n",
                    convertDrawableToByteArray(R.drawable.news2)));
            newsDao.insert(new News(
                    "Perspėja šunų šeimininkus: saugokite augintinius nuo šių pavasarinių augalų",
                    "Prasidėjus pavasariui šunų šeimininkai raginami atkreipti dėmesį į tai, kad tam tikri pavasariniai augalai ir svogūnėliai jų augintiniams gali būti toksiški, o kai kuriais atvejais – ir mirtini.\n",
                    "Labdaros organizacijos „Dogs Trust Shoreham“ vyresniosios veterinarijos chirurgės Charlie Dobson teigimu, tokios gėlės kaip narcizai ir tulpės „gali kelti problemų šunims, ypač tiems, kurie mėgsta kasti sode“, rašo bbc.com. Labdaros organizacija teigia, kad snieguolės, rododendrai, amariliai, azalijos ir melsvadumbliai yra toksiški šunims.\n" + "\n" +
                            "Pasak veterinarijos gydytojos, bendri apsinuodijimo požymiai gali būti vėmimas, viduriavimas, gausus seilėtekis, mieguistumas, o sunkiais atvejais – pasunkėjęs kvėpavimas, drebulys ir net traukuliai.\n"+"\n"+ "Šunims taip pat gali būti nuodingi vėdrynai, krokai, paprastoji raktažolė, ciklamenai, šeivamedžio uogos, gysločių lapai ir sėklos, hiacintų svogūnėliai, lubinų lapai ir sėklos.\n" +
                            "„Kai kuriais atvejais apsinuodijimas pavasariniais svogūnėliais gali būti mirtinas, – sakė M. Dobson ir pridūrė: – Nors apsinuodijimų atvejų pasitaiko retai, žinojimas, kurie augalai yra pavojingi, ir jų laikymas nepasiekiamoje vietoje gali padėti išvengti atsitiktinių apsinuodijimų.“ Labdaros organizacijos teigimu, jeigu šeimininkai įtaria, kad jų augintinis prarijo šių augalų ar pavasarinių svogūnėlių, jie nedelsdami turi kreiptis į veterinarijos gydytoją, net, jei augintiniui nepasireiškia jokių simptomų.\n",
                    convertDrawableToByteArray(R.drawable.news3)));
            newsDao.insert(new News(
                    "Sodininkai įvardijo 5 lauko augalus, kurie gali išgyventi bet ką",
                    "Atkreipkite į jas dėmesį, jei ieškote veislių, kurios gali išgyventi be kruopščios priežiūros. Skirtingai nei kambariniams augalams, kuriems galite skirti asmeninę priežiūrą – reguliuoti temperatūrą, laistymą ir saulės šviesą, lauko augalams paprastai jo skiriama daug mažiau. Kaip rašo Martha Stewart, yra keletas variantų, kurie gali atlaikyti bet kokias atšiaurias sąlygas.",
                    "Gysločiai - Pasak sodo ekspertės Wendy Klusendorf, šis daugiametis augalas gali pakęsti beveik viską, ką jam pasiūlo gamta – nuo pusšešėlio iki visiškos saulės ir prastos dirvos (jis gali augti net sunkiame smėlingame dirvožemyje, kurio nemėgsta dauguma augalų).\n"  +"\n"+ "Pelargonijos - Pelargonijos populiarios ir kaip kambariniai, ir kaip lauko , tačiau jas labai sunku sunaikinti, jei pasodinsite į vietą, kurioje jos mielai auga lauke, aiškina G. Klusendorf. Paprastai tai būna drėgnas dirvožemis ir kelios valandos saulės šviesos per dieną. \n"  +"\n" + "Žolelės - Jei ieškote augalų, galinčių atlaikyti daug pokyčių tiek dėl oro sąlygų, tiek dėl besikeičiančių metų laikų, Hermansonas siūlo rinktis „miškingesnes“ žoleles.\n"  +"\n" + "Vaistažolės - „Tai šalavijai, rozmarinai ir čiobreliai – tai augalai, kuriuos galima nupjauti ir vėl pasodinti“, – aiškina sodininkas ir priduria, kad laiko išbandymą gali atlaikyti ir levandos.\n",
                    convertDrawableToByteArray(R.drawable.news4)));
        }

        loadNews();

        sidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MeniuOverlay.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left, 0);
            }
        });
    }
    public void loadNews(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.main_page_layout);
        NewsDatabase newsDatabase = AppActivity.getNewsDatabase();
        List<News> newsList = newsDatabase.newsDao().getAllNews();

        if (newsList == null || newsList.isEmpty()){return;}

        for (News news : newsList){
            TextView newTextView = new TextView(this);
            newTextView.setText(news.getHeadline());
            newTextView.setTextColor(getResources().getColor(R. color. text_brown));
            newTextView.setTextSize(24);
            newTextView.setTypeface(ResourcesCompat.getCachedFont(this, R.font.spectral_sc));



            newTextView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(getBaseContext(), NewsOverlay.class);
                    intent.putExtra("id", news.getId());
                    startActivity(intent);
                }
            });
            layout.addView(newTextView);

            //Divider
            if (news != newsList.get(newsList.size()-1)){
                ImageButton newImageButton = new ImageButton(this);
                //newImageButton.setPadding(110, 8, 0, 8);
                byte[] imageData = news.getImage();
                if (imageData != null && imageData.length > 0) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                    newImageButton.setImageBitmap(bitmap);
                }
                //newImageButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(400, 400);
                params.setMargins(100, 20, 50, 40);
                newImageButton.setLayoutParams(params);
                newImageButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(getBaseContext(), NewsOverlay.class);
                        intent.putExtra("id", news.getId());
                        startActivity(intent);
                    }
                });
                layout.addView(newImageButton);
            }


        }
    }

    private byte[] convertDrawableToByteArray(int drawableId) {
        Drawable drawable = getResources().getDrawable(drawableId, null);

        if (drawable == null) {
            return null;
        }

        // Convert Drawable to Bitmap
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        // Convert Bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}