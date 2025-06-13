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
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainPage extends BaseActivity {

    private FirebaseFirestore db;
    private LinearLayout layout;
    private ImageButton sidebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contstraint_layout),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });

        db = FirebaseFirestore.getInstance();
        layout = findViewById(R.id.main_page_layout);
        sidebar = findViewById(R.id.sidebar_from_main_page);

        showCurrentUsername();

        seedNewsIfEmpty();

        loadNews();

        sidebar.setOnClickListener(v -> {
            startActivity(new Intent(this, MeniuOverlay.class));
            overridePendingTransition(R.anim.slide_out_left, 0);
        });
    }

    private void showCurrentUsername() {
        TextView usernameTv = findViewById(R.id.main_page_username);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        db.collection("users")
                .document(currentUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            String name = doc.getString("username");
                            usernameTv.setText(name);
                        }
                    } else {
                        Log.e("MainPage", "Failed to load username", task.getException());
                    }
                });
    }
    private void seedNewsIfEmpty() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("news_images");

        db.collection("news").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().isEmpty()) {
                List<News> seedList = new ArrayList<>();

                seedList.add(new News(
                        "8 mažai priežiūros reikalaujantys kambariniai augalai: juos itin sunku numarinti",
                        "Jei jums sunkiai sekasi tinkamai puoselėti augalus, tačiau vis tiek norite jų turėti savo namuose, yra išeitis. Pasirodo, kai kurie augalai beveik nereikalauja priežiūros, o tai reiškia, kad jų beveik neįmanoma numarinti.",
                        "Kambariniai augalai paprastai namams suteikia grožio, spalvų ir gyvybiškumo, tačiau ne visada lengva jais pasirūpinti. Viena dažniausiai kylančių problemų – nepakankamas laistymas arba, priešingai, perlaistymas, rašoma puslapyje „Better Homes&Gardens“. Visgi dalis kambarinių augalų nereikalauja daug priežiūros ir yra itin atlaidūs – jų kone neįmanoma numarinti.\n"  +"\n" + "Vėzdūnė Vienas iš populiarių pasirinkimų patalpoms – vėzdūnė, kuri pakenčia silpną apšvietimą, mažą drėgmę ir nepastovų laistymą. Žydi dažniausiai vasarą, bet gali pražysti bet kuriuo metų laiku.\n"  +"\n" +"Trijuostė sansevjera Šis augalas gerai auga beveik bet kurioje patalpoje. Lapai paprastai būna pilkai žalios spalvos, taip pat gali būti su geltonais ar baltais apvadais. Gali augti silpnoje šviesoje, tačiau geriau sekasi vidutinėje arba ryškioje šviesoje.",
                        convertDrawableToByteArray(R.drawable.news1)));
                seedList.add(new News("Paragavęs namuose augančio augalo, katinas Jeronimas patyrė tikrą košmarą: sveikata sutriko vos nuo kelių lapų",
                                "Vilnietė Asta prismena išgąstį, kai vieną dieną jos kruopščiai iš duonvaisio sėklos išauginti daigeliai netikėtai prarado lapus. Įtarimai greitai krito ant garbaus amžiaus šeimos numylėtinio – aštuoniolikmečio  Jeronimo, kuris, kaip pasakoja ji, neatsispiria žaliuojančiai augmenijai. Neilgai trukus katinas ėmė negaluoti, o šiandien moteris džiaugiasi, kad viskas baigėsi laimingai, ir ragina kačių šeimininkus atidžiai rinktis namuose auginamus augalus, jog netektų stebėti augintinio kančių.",
                                "Asta pasakoja sumaniusi iš duonvaisio sėklų išauginti augalus, mat vaisius egzotinis ir įdomus, todėl ir daiginimo procesas jai pasirodė vertas dėmesio. Tada ji nesusimąstė, kad tai gali tapti didžiule problema. Sėklos sėkmingai išleido daigelius, tačiau trys daigeliai įtartinai augo nevienodai – vienas daigas greitai apsirėdyjo lapais, kiti – liko stagarais. Vis dėlto, dabar moteris svarsto, kad taip nutiko ne veltui – lapuotas daigas stovėjo atokioje, katinui nepasiekiamoje vietoje, kiti du – atviroje ir katinui prieinamoje teritorijoje.\n" + "\n" + "„Tik Jeronimui pradėjus negaluoti ir radus lapą ant grindų supratau, kad tuos du augalus galėjo nukramtyti jis. Iki šiol to nepastebėjau – maniau, kad jie tiesiog prastai auga, gal netiko vieta ar tiesiog nebuvo lemta“, – pasakoja ji.\n" + "\n" + "Asta prisimena, kad Jeronimas 2,5 valandos neatsitraukdamas lakė vandenį iš tekančio krano, o viduriai buvo užkietėję taip, kad katinas tuštinosi net klykdamas. Laimei, jo būklė po kelių valandų pagerėjo, o katinas dabar sveikas, kaip iki šiol. \n",convertDrawableToByteArray(R.drawable.news2)
                                ));
                seedList.add(new News("Sodininkai įvardijo 5 lauko augalus, kurie gali išgyventi bet ką",
                        "Atkreipkite į jas dėmesį, jei ieškote veislių, kurios gali išgyventi be kruopščios priežiūros. Skirtingai nei kambariniams augalams, kuriems galite skirti asmeninę priežiūrą – reguliuoti temperatūrą, laistymą ir saulės šviesą, lauko augalams paprastai jo skiriama daug mažiau. Kaip rašo Martha Stewart, yra keletas variantų, kurie gali atlaikyti bet kokias atšiaurias sąlygas.",
                        "Gysločiai - Pasak sodo ekspertės Wendy Klusendorf, šis daugiametis augalas gali pakęsti beveik viską, ką jam pasiūlo gamta – nuo pusšešėlio iki visiškos saulės ir prastos dirvos (jis gali augti net sunkiame smėlingame dirvožemyje, kurio nemėgsta dauguma augalų).\n"  +"\n"+ "Pelargonijos - Pelargonijos populiarios ir kaip kambariniai, ir kaip lauko , tačiau jas labai sunku sunaikinti, jei pasodinsite į vietą, kurioje jos mielai auga lauke, aiškina G. Klusendorf. Paprastai tai būna drėgnas dirvožemis ir kelios valandos saulės šviesos per dieną. \n"  +"\n" + "Žolelės - Jei ieškote augalų, galinčių atlaikyti daug pokyčių tiek dėl oro sąlygų, tiek dėl besikeičiančių metų laikų, Hermansonas siūlo rinktis „miškingesnes“ žoleles.\n"  +"\n" + "Vaistažolės - „Tai šalavijai, rozmarinai ir čiobreliai – tai augalai, kuriuos galima nupjauti ir vėl pasodinti“, – aiškina sodininkas ir priduria, kad laiko išbandymą gali atlaikyti ir levandos.\n",
                        convertDrawableToByteArray(R.drawable.news3)));
                seedList.add(new News(
                        "Perspėja šunų šeimininkus: saugokite augintinius nuo šių pavasarinių augalų",
                        "Prasidėjus pavasariui šunų šeimininkai raginami atkreipti dėmesį į tai, kad tam tikri pavasariniai augalai ir svogūnėliai jų augintiniams gali būti toksiški, o kai kuriais atvejais – ir mirtini.\n",
                        "Labdaros organizacijos „Dogs Trust Shoreham“ vyresniosios veterinarijos chirurgės Charlie Dobson teigimu, tokios gėlės kaip narcizai ir tulpės „gali kelti problemų šunims, ypač tiems, kurie mėgsta kasti sode“, rašo bbc.com. Labdaros organizacija teigia, kad snieguolės, rododendrai, amariliai, azalijos ir melsvadumbliai yra toksiški šunims.\n" + "\n" +
                                "Pasak veterinarijos gydytojos, bendri apsinuodijimo požymiai gali būti vėmimas, viduriavimas, gausus seilėtekis, mieguistumas, o sunkiais atvejais – pasunkėjęs kvėpavimas, drebulys ir net traukuliai.\n"+"\n"+ "Šunims taip pat gali būti nuodingi vėdrynai, krokai, paprastoji raktažolė, ciklamenai, šeivamedžio uogos, gysločių lapai ir sėklos, hiacintų svogūnėliai, lubinų lapai ir sėklos.\n" +
                                "„Kai kuriais atvejais apsinuodijimas pavasariniais svogūnėliais gali būti mirtinas, – sakė M. Dobson ir pridūrė: – Nors apsinuodijimų atvejų pasitaiko retai, žinojimas, kurie augalai yra pavojingi, ir jų laikymas nepasiekiamoje vietoje gali padėti išvengti atsitiktinių apsinuodijimų.“ Labdaros organizacijos teigimu, jeigu šeimininkai įtaria, kad jų augintinis prarijo šių augalų ar pavasarinių svogūnėlių, jie nedelsdami turi kreiptis į veterinarijos gydytoją, net, jei augintiniui nepasireiškia jokių simptomų.\n",
                        convertDrawableToByteArray(R.drawable.news4)));

                for (News news : seedList) {
                    byte[] imageData = news.getImage();
                    String filename = UUID.randomUUID().toString() + ".png";
                    StorageReference imgRef = storageRef.child(filename);

                    imgRef.putBytes(imageData)
                            .addOnSuccessListener(upload -> imgRef.getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                        String url = uri.toString();
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("headline", news.getHeadline());
                                        map.put("summary", news.getMain_text());
                                        map.put("text", news.getFull_text());
                                        map.put("imageUrl", url);
                                        map.put("timestamp", FieldValue.serverTimestamp());

                                        db.collection("news")
                                                .add(map)
                                                .addOnSuccessListener(r -> Log.d("SeedNews", "Added: " + r.getId()))
                                                .addOnFailureListener(e -> Log.w ("SeedNews", "Fail add", e));
                                    })
                                    .addOnFailureListener(e -> Log.e("SeedNews", "Fail URL", e))
                            )
                            .addOnFailureListener(e -> Log.e("SeedNews", "Upload fail", e));
                }
            }
        });
    }
    private void loadNews() {
        layout.removeAllViews();

        db.collection("news")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("LoadNews", "Error: ", task.getException());
                        return;
                    }
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String headline = doc.getString("headline");
                        String summary  = doc.getString("summary");
                        String text     = doc.getString("text");
                        String imageUrl = doc.getString("imageUrl");

                        // --- Teksto antraštė
                        TextView tv = new TextView(MainPage.this);
                        tv.setText(headline);
                        tv.setTextColor(getResources().getColor(R.color.text_brown));
                        tv.setTextSize(24);
                        tv.setTypeface(ResourcesCompat.getCachedFont(MainPage.this, R.font.spectral_sc));
                        tv.setOnClickListener(v -> {
                            Intent i = new Intent(MainPage.this, NewsOverlay.class);
                            i.putExtra("headline", headline);
                            i.putExtra("summary", summary);
                            i.putExtra("text", text);
                            i.putExtra("imageUrl", imageUrl);
                            i.putExtra("id", doc.getId());
                            startActivity(i);
                        });
                        layout.addView(tv);

                        // --- Paveikslėlis per Glide
                        ImageView iv = new ImageView(MainPage.this);
                        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(400, 400);
                        p.setMargins(100, 20, 50, 40);
                        iv.setLayoutParams(p);
                        Glide.with(this)
                                .load(imageUrl)
                                .placeholder(R.drawable.news1)
                                .into(iv);

                        iv.setOnClickListener(v -> {
                            Intent i = new Intent(MainPage.this, NewsOverlay.class);
                            i.putExtra("headline", headline);
                            i.putExtra("summary", summary);
                            i.putExtra("text", text);
                            i.putExtra("imageUrl", imageUrl);
                            i.putExtra("id", doc.getId());
                            startActivity(i);
                        });
                        layout.addView(iv);
                    }
                });
    }

    private byte[] convertDrawableToByteArray(int drawableId) {
        Drawable d = getResources().getDrawable(drawableId, null);
        if (d == null) return new byte[0];

        Bitmap bitmap;
        if (d instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) d).getBitmap();
        } else {
            bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            d.setBounds(0, 0, c.getWidth(), c.getHeight());
            d.draw(c);
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }
}