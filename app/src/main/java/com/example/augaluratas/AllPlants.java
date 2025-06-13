package com.example.augaluratas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import android.content.Intent;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AllPlants extends BaseActivity {
    private LinearLayout plantsContainer;
    private SearchView searchbar;
    private NestedScrollView scrollView;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private List<Plants> allPlants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_plants);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Init
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        plantsContainer = findViewById(R.id.plantsContainer);
        searchbar = findViewById(R.id.all_plants_searchbar);
        scrollView = findViewById(R.id.all_plants_scrollview);

        // Seed if empty
        seedPlantsIfEmpty();

        // Load and display
        loadPlantsFromFirestore();

        // Search listeners
        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String q) { filterAndDisplayPlants(q); return false; }
            @Override public boolean onQueryTextChange(String t) { filterAndDisplayPlants(t); return false; }
        });
        searchbar.setOnCloseListener(() -> { filterAndDisplayPlants(""); return false; });
        scrollView.setOnTouchListener((v, e) -> { searchbar.clearFocus(); return false; });

        // Sidebar
        findViewById(R.id.sidebar_from_all_plants).setOnClickListener(v -> {
            startActivity(new Intent(this, MeniuOverlay.class));
            overridePendingTransition(R.anim.slide_out_left, 0);
        });
    }

    private void seedPlantsIfEmpty() {
        StorageReference imagesRef = storage.getReference().child("plant_images");
        db.collection("plants").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().isEmpty()) {
                // Paruošiame pradinį sąrašą
                List<Plants> seed = new ArrayList<>();
               // seed.add(new Plants("Chlorofilas Variegatum", "Labai nereikli...", "Chlorofitai", convertDrawableToByteArray(R.drawable.chlorofilas_variegatum)));
                //seed.add(new Plants("Monstera Obliqua Peru", "Lengvai ir greitai...", "Monsteros", convertDrawableToByteArray( R.drawable.monstera_obliqua_peru)));
                seed.add(new Plants(
                        "Monstera Thai",
                        "Lengvai ir greitai augantis kambarinis augalas, gali užaugti didžiulis ir gyventi daug metų. Pritaikomas įvairiuose interjeruose. Užaugina įspūdingo dydžio, blizgančius, giliai skeldėtus lapus.\n" +"\n" +
                                "Laikymas: saulėta kambario vieta, rytinė arba vakarinė pusė. Vasarą galima išnešti į lauką.\n" +"\n" +
                                "Apšvietimas: netiesioginė dienos šviesa, toleruoja prieblandą.\n" +"\n" +
                                "Temperatūra: 15–24 °C.\n" +"\n" +
                                "Laistymas: mėgsta drėgmę.\n" +"\n" +
                                "Substratas: derlingas, humusingas. Reikalingas geras drenažas.\n" +"\n" +
                                "Tręšimas: kartą per mėnesį lapinių gėlių trąšomis.\n" +"\n" +
                                "Persodinimas: kas dvejus metus.\n" +"\n" +
                                "Patarimas: jeigu trukdo monsteros orinės šaknys, galima jas apgenėti arba suvynioti atgal į vazoną. Nuolat pasirūpinti, kad lapai neapdulkėtų.",
                        "Monsteros",
                        convertDrawableToByteArray(R.drawable.monstera_thai)));
                seed.add(new Plants(
                        "Alokazija Polly",
                        "Azijos atogrąžų regiono augalas, išsiskiriantis lapų grožiu. Spalvingi lapai išauga ant aukštų, tiesių stiebų.\n" +"\n" +
                                "Apšvietimas: mėgsta šviesą, tačiau saugoti nuo tiesioginių saulės spindulių.\n" +"\n" +
                                "Temperatūra: 16–25 °C.\n" +"\n" +
                                "Laistymas: vidutiniškas, žemė turi pradžiūti iki kito laistymo. Naudoti minkštą kambario temperatūros vandenį. Mėgsta santykinę drėgmę, todėl galima purkšti ir lapus.\n" +"\n" +
                                "Ramybės laikotarpis: rečiau laistyti ir netręšti.\n" +"\n" +
                                "Substratas: laidus, derlingas.\n" +"\n" +
                                "Tręšimas: kas 2 savaites silpnu trąšų tirpalu.\n" +"\n" +
                                "Genėjimas: gėlė nemedėjanti, todėl išskinami tik pažeisti ar sergantys lapai.\n" +"\n" +
                                "Persodinimas: tik pavasarį, kas 2–3 metus.\n" +"\n",
                        "Alokazijos",
                        convertDrawableToByteArray(R.drawable.alokazija_polly)));
                seed.add(new Plants(
                        "Zamiokuklas",
                        "Viena iš populiariausių kambarinių gėlių, dar vadinama pinigų medžiu. Auga mėsingais, storais lapkočiais ir tamsiai žaliais, blizgančiais lapais su nusmailėjusiomis viršūnėmis.\n" +"\n" +
                                "Laikymas: šiltos ir šviesios erdvės.\n" +"\n" +
                                "Apšvietimas: gerai auga ryškioje šviesoje, tačiau pakečia ir dalinį pavėsį.\n" +"\n" +
                                "Temperatūra: 17–25 °C.\n" +"\n" +
                                "Laistymas: nuo pavasario iki rudens vidutiniškas. Žiemą laistomas retai.Nemėgsta užmirkimo.\n" +"\n" +
                                "Substratas: derlingas, purus, smėlinga. Reikalingas geras drenažas.\n" +"\n" +
                                "Tręšimas: augimo metu kas dvi savaites lapinių gėlių trąšomis.\n" +"\n" +
                                "Persodinimas: kiekvienais metais.",
                        "Zamiokuklai",
                        convertDrawableToByteArray(R.drawable.zamiokuklas)));
                seed.add(new Plants(
                        "Anturis Esudo",
                        "Gana nereikli, net 3–4 mėnesius žydinti gėlė, turinti įdomią lapų formą, kuri augalą paverčia nuolat dekoratyviu.\n" +"\n" +
                                "Laikymas: gerai vėdinamoje patalpoje, toliau nuo šildymo radiatorių.\n" +"\n" +
                                "Apšvietimas: gerai apšviesta, tačiau apsaugota nuo tiesioginių saulės spindulių.\n" +"\n" +
                                "Temperatūra: 14–22 °C.\n" +"\n" +
                                "Laistymas: žydėjimo metu dažnai, žemė turi būti visada drėgna.\n" +"\n" +
                                "Ramybės laikotarpis: rudens pabaiga ir žiemos pradžia.\n" +"\n" +
                                "Substratas: mėgsta purią, smėlingą žemę. Reikalingas geras drenažas.\n" +"\n" +
                                "Tręšimas: nuo gegužės skystomis mineralinėmis arba organinėmis trąšomis, kas 2–3 savaites.\n" +"\n" +
                                "Persodinimas: jauni augalai persodinami kasmet, senesni – kas 2–3 metus.",
                        "Anturiai",
                        convertDrawableToByteArray(R.drawable.anturis_esudo)));
                seed.add(new Plants(
                        "Jazminaitis Pink Bow",
                        "Stipriai kvepiantis vijoklinis kambarinis augalas, kilęs iš Kinijos. Priklauso alyvmedinių šeimai. Gausiai žydi baltos - rausvos spalvos žiedeliais. Žali, plunksniški lapeliai.\n" +"\n" +
                                "Laikymas: rytinėje arba pietinėje pusėje. Saugoti nuo šalto oro gūsių, nes jautriai reaguoja į temperatūros pokyčius.\n" +"\n" +
                                "Apšvietimas: vasarą saugoti nuo tiesioginių saulės spindulių, gali augti ir pavėsingoje vietoje.\n" +"\n" +
                                "Temperatūra: 15–25 °C.\n" +"\n" +
                                "Laistymas: Vanduo turi būti minkštas, kambario temperatūros. Substratas nuolat drėgnas.\n" +"\n" +
                                "Ramybės laikotarpis: žiemą laistoma rečiau.\n" +"\n" +
                                "Substratas: smėlingas arba durpės, pH 5,5–6,5. Reikalingas geras drenažas, nes nepakenčia užmirkimo.\n" +"\n" +
                                "Tręšimas: pavasario–rudens laikotarpiu kartą per mėnesį mineralinėmis žydinčių augalų trąšomis.\n" +"\n" +
                                "Genėjimas: reikalingas, norint formuoti taisyklingą lają. Auga labai greitai - vijokliu todėl reikalingos atramos.\n" +"\n" +
                                "Persodinimas: kas 2–3 metus.",
                        "Jazminaičiai",
                        convertDrawableToByteArray(R.drawable.jazminaitis_pink_bow)));
                seed.add(new Plants(
                        "Vėzdūnė",
                        "Natūraliai auga Amerikos miškuose. Dekoratyvus augalas ilgakočiais lapais bei balta pažiede, o žiedas panašus į burbuolę. Nereiklus ir lengvai auginamas namų sąlygomis.\n" +"\n" +
                                "Laikymas: pietinė ar pietrytinė pusė, nepakenčia skersvėjų.\n" +"\n" +
                                "Apšvietimas: nemėgsta tiesioginių saulės spindulių.\n" +"\n" +
                                "Temperatūra: 18–25 °C.\n" +"\n" +
                                "Laistymas: mėgsta drėgmę, ypač augimo metu, galima purkšti lapus. Tačiau nepakenčia užmirkimo.\n" +"\n" +
                                "Substratas: durpinis, sumaišytas su kompostu, netgi smulkaus žvyro, kad būtų geresnis drenažas.\n" +"\n" +
                                "Tręšimas: kas dvi savaites augimo metu.\n" +"\n" +
                                "Persodinimas: kas 1–2 metus. Mėgsta erdvę, todėl vazonas gali būti ir didesnis.",
                        "Vėzdūnės",
                        convertDrawableToByteArray(R.drawable.vezdune)));
                seed.add(new Plants(
                        "Azalija Mix On Stem",
                        "Viržinių šeimos visžalis kambarinis augalas, natūraliai augantis Japonijoje ir Kinijoje. Puošia namų aplinką įvairiaspalviais žiedais – rožiniais, baltais, margais. Dažniausiai sužydi taip gausiai, kad nebesimato net augalo lapų.\n" +"\n" +
                                "Laikymas: pietinė arba vakarinė pusė. Vasarą galima išnešti į lauką, tačiau laikyti pavėsingoje vietoje.\n" +"\n" +
                                "Apšvietimas: reikia daug šviesos, tinka ir tiesioginiai saulės spinduliai. Jeigu pradeda trūkti šviesos, augalas skursta.\n" +"\n" +
                                "Temperatūra: 18–20 °C.\n" +"\n" +
                                "Laistymas: vidutiniškas, tačiau netoleruoja perlaistymo, kartą per 2 savaites. Vanduo turi būti minkštas, kambario temperatūros. Nepakenčia užmirkimo ir sauso oro.\n" +"\n" +
                                "Ramybės laikotarpis: žiemą laistyti dar rečiau, palaikyti 8–10 °C temperatūrą.\n" +"\n" +
                                "Substratas: specialus, skirtas azalijoms. Reikalingas geras drenažas, nes nepakenčia užmirkimo.\n" +"\n" +
                                "Tręšimas: pavasario–rudens laikotarpiu kartą per mėnesį tręšti erikinių augalų trąšomis.\n" +"\n" +
                                "Genėjimas: nereikalingas, nebent norima formuoti taisyklingą puskrūmį.\n" +"\n" +
                                "Persodinimas: kas 3 metus.",
                        "Azalijos",
                        convertDrawableToByteArray(R.drawable.azalija_mix_in_stem)));
                seed.add(new Plants(
                        "Gloksinija",
                        "Šakniagumbinė gėlė kilusi iš Brazilijos tropinių miškų. Lapai ovalūs, apaugę plaukeliais. Žiedai labai dideli, varpelio formos, gali būti rožinės, raudonos, violetinės spalvos. Žydi nuo balandžio – rugpjūčio mėn.\n" +"\n" +
                                "Laikymas: labai svarbu užtikrinti oro drėgmę, nepakenčia skersvėjo.\n" +"\n" +
                                "Apšvietimas: reikalingas geras apšvietimas, tačiau nepakenčia tiesioginių saulės spindulių.\n" +"\n" +
                                "Temperatūra: 16–28 °C.\n" +"\n" +
                                "Laistymas: mėgsta drėgmę, ypač vasaros metu būtinas dažnas laistymas, nepakenčia užmirkimo. Reikalingas geras drenažas.\n" +"\n" +
                                "Substratas: kompostinis, trąšus, pH 5,5–6,5.\n" +"\n" +
                                "Tręšimas: žydinčių gėlių trąšomis kas 2-3 savaites.\n" +"\n" +
                                "Ramybės periodas: rugsėjo-spalio mėn. mažiau lieti, lapai nuvysta ir juos reikia nupjauti. Gumbai paliekami vazonuose ir laikomi 10 °C temperatūroje, kartais paliejame, kad gumbai nesuvystų.\n" +"\n" +
                                "Genėjimas: nereikalingas.",
                        "Gloksinijos",
                        convertDrawableToByteArray(R.drawable.gloksinija)));
                seed.add(new Plants(
                        "Karduote Moonshine",
                        "Vienas griežčiausių augalų, kurie savo struktūra puošia interjerą ir gali nudžiuginti savo griežta, stora lapija. Galima rasti vienspalvių, margų veislių. Labai nereiklios, todėl kiekvienas gali juos auginti.\n" +"\n" +
                                "Laikymas: gerai jaučiasi kambaryje, kuriame mažiau šviesos. Tinka rytinė arba vakarinė pusė. Vasarą atgaivinti galima išnešti į lauką.\n" +"\n" +
                                "Apšvietimas: reikalinga šviesa, tačiau jautriai reaguoja į tiesioginius spindulius.\n" +"\n" +
                                "Temperatūra: reikalinga 15–20 °C.\n" +"\n" +
                                "Laistymas: tik tada, kai substratas visiškai išdžiūsta. Rekomenduojama kas 2–6 savaites, atsižvelgiant į namų drėgmę ir temperatūrą.\n" +"\n" +
                                "Substratas: gerai drenuojamas, derlingas, gali būti naudojamas kaktusinių augalų mišinys.\n" +"\n" +
                                "Tręšimas: augimo metu skystomis kaktusų trąšomis. Žiemą netręšiama.\n" +"\n" +
                                "Persodinimas: kovą arba balandį, kai augalas visiškai užpildo vazoną.",
                        "Karduotės",
                        convertDrawableToByteArray(R.drawable.karduote_moonshine)));
                seed.add(new Plants(
                        "Alokazija Chocolate Green",
                        "Azijos atogrąžų regiono augalas, išsiskiriantis lapų grožiu. Spalvingi lapai išauga ant aukštų, tiesių stiebų.\n" +"\n" +
                                "Apšvietimas: mėgsta šviesą, tačiau saugoti nuo tiesioginių saulės spindulių.\n" +"\n" +
                                "Temperatūra: 16–25 °C.\n" +"\n" +
                                "Laistymas: vidutiniškas, žemė turi pradžiūti iki kito laistymo. Naudoti minkštą kambario temperatūros vandenį. Mėgsta santykinę drėgmę, todėl galima purkšti ir lapus.\n" +"\n" +
                                "Ramybės laikotarpis: rečiau laistyti ir netręšti.\n" +"\n" +
                                "Substratas: laidus, derlingas.\n" +"\n" +
                                "Tręšimas: kas 2 savaites silpnu trąšų tirpalu.\n" +"\n" +
                                "Genėjimas: gėlė nemedėjanti, todėl išskinami tik pažeisti ar sergantys lapai.\n" +"\n" +
                                "Persodinimas: tik pavasarį, kas 2–3 metus.",
                        "Alokazijos",
                        convertDrawableToByteArray(R.drawable.alokazija_chocolate_green)));
                seed.add(new Plants(
                        "Alokazija Black Velvet",
                        "Azijos atogrąžų regiono augalas, išsiskiriantis lapų grožiu. Spalvingi lapai išauga ant aukštų, tiesių stiebų.\n" +"\n" +
                                "Apšvietimas: mėgsta šviesą, tačiau saugoti nuo tiesioginių saulės spindulių.\n" +"\n" +
                                "Temperatūra: 16–25 °C.\n" +"\n" +
                                "Laistymas: vidutiniškas, žemė turi pradžiūti iki kito laistymo. Naudoti minkštą kambario temperatūros vandenį. Mėgsta santykinę drėgmę, todėl galima purkšti ir lapus.\n" +"\n" +
                                "Ramybės laikotarpis: rečiau laistyti ir netręšti.\n" +"\n" +
                                "Substratas: laidus, derlingas.\n" +"\n" +
                                "Tręšimas: kas 2 savaites silpnu trąšų tirpalu.\n" +"\n" +
                                "Genėjimas: gėlė nemedėjanti, todėl išskinami tik pažeisti ar sergantys lapai.\n" +"\n" +
                                "Persodinimas: tik pavasarį, kas 2–3 metus.",
                        "Alokazijos",
                        convertDrawableToByteArray(R.drawable.alokazija_black_velvet)));
                seed.add(new Plants(
                        "Filodendras Choco Red",
                        "Viena gražiausių kambarinių lapinių gėlių, stebinanti savo įvairove nuo lapų formos ir dydžio iki spalvų gamos. Yra net vijoklinių rūšių.\n" +"\n" +
                                "Apšvietimas: mėgsta šviesą, tačiau toleruoja ir dalinį pavėsį. Gali prisitaikyti ir prie tiesioginės saulės spindulių.\n" +"\n" +
                                "Temperatūra: 13–25 °C.\n" +"\n" +
                                "Laistymas: dažnai, ypač augimo metu. Stebėti, kad substratas neišdžiūtų.\n" +"\n" +
                                "Substratas: laidus, derlingas.\n" +"\n" +
                                "Tręšimas: intensyviu augimo metu kas savaitę skystomis trąšomis.\n" +"\n" +
                                "Genėjimas: galimas, norint riboti augalo dydį ar sutankinti kerą.\n" +"\n" +
                                "Persodinimas: kai kurių rūšių filodendrai labai greitai auga, todėl gali prireikti persodinti kasmet. Vijoklinėms rūšims parūpinti atramas.",
                        "Filodendrai",
                        convertDrawableToByteArray(R.drawable.filodendras_choco_red)));
                seed.add(new Plants(
                        "Kalankė Tomentosa Chocolate",
                        "Sukulentinė, ypač ilgai žydinti gėlė, kuri gali džiuginti įvairiaspalviais žiedais visus metus. Kilusi iš Madagaskaro, puikiai auga ir mūsų namų sąlygomis.\n" +"\n" +
                                "Laikymas: vakarinė ir rytinė palangė. Žiemą perkelti ant pietinės.\n" +"\n" +
                                "Apšvietimas: mėgsta šviesią vietą, tačiau nepakenčia tiesioginių saulės spindulių.\n" +"\n" +
                                "Temperatūra: 12–30 °C.\n" +"\n" +
                                "Laistymas: pavasario–rudens laikotarpiu saikingai. Nepakenčia užmirkimo.\n" +"\n" +
                                "Substratas: durpės, sumaišytos su smėliu.\n" +"\n" +
                                "Tręšimas: azoto, fosforo ir kalio trąšomis, geriausia rinktis skysto tipo.\n" +"\n" +
                                "Pražydinimas: po žydėjimo žiedai nukerpami ir vazonėlis pastatomas silpniau apšviestoje vietoje. Mėnesį nelaistoma, vėliau perstatoma į nuolatinę vietą ir vėl pradedama saikingai laistyti.\n" +"\n" +
                                "Persodinimas: ankstyvą pavasarį.",
                        "Kalankės",
                        convertDrawableToByteArray(R.drawable.kalanke_tomentosa_chocolate)));
                seed.add(new Plants(
                        "Alavijas Humilis Variegata",
                        "Aloe vera yra sukulentinis augalas, garsėjantis ne tik savo išvaizda, bet ir gydomosiomis savybėmis. Šis augalas, natūraliai augantis sausose vietose, puikiai tinka dekoruoti įvairias patalpas ir suteikti joms išskirtinį charakterį.\n" +"\n" +
                                "Laikymas: Idealus auginti ant rytinės arba vakarinės palangės. Vasara alaviją galima perkelti į lauką, kad gautų daugiau natūralios šviesos.\n" +"\n" +
                                "Apšvietimas: Alavijas mėgsta saulėtą vietą ir daug šviesos.\n" +"\n" +
                                "Temperatūra: Geriausia laikyti esant temperatūrai iki 22 °C.\n" +"\n" +
                                "Laistymas: Laistyti retai, tik tada, kai substratas visiškai išdžiūsta. Vandens neturėtų patekti ant lapų, kad jie nesupūtų.\n" +"\n" +
                                "Substratas: Tinkamiausias yra specialus sukulentams skirtas substratas su dideliu smėlio ir žvyro kiekiu, kad užtikrintų gerą vandens pralaidumą.\n" +"\n" +
                                "Tręšimas: Pavasario ir rudens laikotarpiu naudoti trąšas sukulentams ir kaktusams.\n" +"\n" +
                                "Persodinimas: Jauni alavijai turėtų būti persodinami kasmet, o vėliau tai daryti tik pagal poreikį, kai augalas pasieks tam tikrą dydį.",
                        "Alavijai",
                        convertDrawableToByteArray(R.drawable.alavijas_humilis_variegata)));
                seed.add(new Plants(
                        "Karduotė Superba",
                        "Vienas griežčiausių augalų, kurie savo struktūra puošia interjerą ir gali nudžiuginti savo griežta, stora lapija. Galima rasti vienspalvių, margų veislių. Labai nereiklios, todėl kiekvienas gali juos auginti.\n" +"\n" +
                                "Laikymas: gerai jaučiasi kambaryje, kuriame mažiau šviesos. Tinka rytinė arba vakarinė pusė. Vasarą atgaivinti galima išnešti į lauką.\n" +"\n" +
                                "Apšvietimas: reikalinga šviesa, tačiau jautriai reaguoja į tiesioginius spindulius.\n" +"\n" +
                                "Temperatūra: reikalinga 15–20 °C.\n" +"\n" +
                                "Laistymas: tik tada, kai substratas visiškai išdžiūsta. Rekomenduojama kas 2–6 savaites, atsižvelgiant į namų drėgmę ir temperatūrą.\n" +"\n" +
                                "Substratas: gerai drenuojamas, derlingas, gali būti naudojamas kaktusinių augalų mišinys.\n" +"\n" +
                                "Tręšimas: augimo metu skystomis kaktusų trąšomis. Žiemą netręšiama.\n" +"\n" +
                                "Persodinimas: kovą arba balandį, kai augalas visiškai užpildo vazoną.",
                        "Karduotės",
                        convertDrawableToByteArray(R.drawable.karduote_superba)));
                seed.add(new Plants(
                        "Storalapis Crassula Mix",
                        "Vienas iš augalų, kuriam nereikia daug priežiūros. Dekoratyvus savo mėsingais, storais, blizgančiais, žaliais lapais.\n" +"\n" +
                                "Laikymas: rytinė ar pietrytinė palangė.\n" +"\n" +
                                "Apšvietimas: tinka pusiau saulėta vieta, nepakenčia tiesioginių saulės spindulių.\n" +"\n" +
                                "Temperatūra: 15–30 °C.\n" +"\n" +
                                "Laistymas: kartą per savaitę, substratas tarp laistymų turi pradžiūti. Dažniausiai apie drėgmės trūkumą išduoda susiraukšlėję lapai. Vasarą mėgsta purškimą per lapus.\n" +"\n" +
                                "Ramybės laikotarpis: norint sulaukti žiedų, perkeliamas į vėsesnę vietą, kur būna 13–14 °C, dviem savaitėms.\n" +"\n" +
                                "Substratas: laidus, derlingas, smėlingas, būtinas geras drenažas.\n" +"\n" +
                                "Tręšimas: kas 2 savaites sukulentinių augalų mineralinėmis trąšomis.\n" +"\n" +
                                "Genėjimas: periodiškai reikia formuoti vainiką.\n" +"\n" +
                                "Persodinimas: nemėgsta, todėl geriausia iškart sodinti į didesnį vazoną.\n" +"\n" +
                                "*Prekės nuotrauka yra informacinio pobūdžio. Originali prekės spalva ir matmenys gali skirtis nuo nuotraukoje pavaizduotos.",
                        "Storalapiai",
                        convertDrawableToByteArray(R.drawable.storalapis_mix)));
                seed.add(new Plants(
                        "Kaktusas Parodija",
                        "Kilusi iš kaktusinių šeimos, todėl ir auginimo sąlygos panašios. Parodija – kiek neįprastas pavadinimas, tačiau miniatiūrinis augalas pakeri savo grožiu. Spygliai tankūs ir gali būti įvairių spalvų bei formų. Žydi nuo ankstyvo pavasario iki rudens. Žiedų įvairovė taip pat gausi: nuo geltonos iki skaisčiai raudonos spalvos.\n" +"\n" +
                                "Laikymas: rytinė arba pietinė pusė.\n" +"\n" +
                                "Apšvietimas: vieta šviesi, tačiau saugoti nuo tiesioginių saulės spindulių.\n" +"\n" +
                                "Temperatūra: 16–25 °C.\n" +"\n" +
                                "Laistymas: kai parodija žydi ir auga, reikia laistyti dažnai, negalima leisti substratui išdžiūti. Vanduo turi būti minkštas. Vazone negali būti vandens pertekliaus.\n" +"\n" +
                                "Ramybės laikotarpis: žiemą laistoma rečiau ir vidutinė temperatūra sumažinama.\n" +"\n" +
                                "Substratas: kompostinis, smėlingas arba durpės,\n" +"\n" +
                                "pH 5,5–6,5. Reikalingas geras drenažas.\n" +"\n" +
                                "Tręšimas: pavasario–rudens laikotarpiu 2 kartus per mėnesį tręšti sukulentinių augalų trąšomis.\n" +"\n" +
                                "Genėjimas: nebūtinas.\n" +"\n" +
                                "Persodinimas: kas 3 metus.",
                        "Kaktusai",
                        convertDrawableToByteArray(R.drawable.kaktusas_parodija)));
                seed.add(new Plants(
                        "Mamiliarija Albata",
                        "Kaktusų genčiai priklausanti mamiliarija pakeri savo grožiu – žiedai įvairiausių spalvų ir išsidėsto vainiku aplink augalo viršūnę. Natūraliai auga Meksikoje, uolėtose vietose.\n" +"\n" +
                                "Laikymas: rytinė arba pietinė pusė.\n" +"\n" +
                                "Apšvietimas: vieta šviesi, tačiau saugoti nuo tiesioginių saulės spindulių.\n" +"\n" +
                                "Temperatūra: 16–25 °C.\n" +"\n" +
                                "Laistymas: kai mamiliarija žydi ir auga, reikia laistyti dažnai, negalima leisti substratui išdžiūti. Vanduo turi būti minkštas. Vazone negali būti vandens pertekliaus.\n" +"\n" +
                                "Ramybės laikotarpis: žiemą laistoma rečiau ir vidutinė temperatūra sumažinama.\n" +"\n" +
                                "Substratas: kompostinis, smėlingas arba durpės,\n" +"\n" +
                                "pH 5,5–6,5. Reikalingas geras drenažas.\n" +"\n" +
                                "Tręšimas: pavasario–rudens laikotarpiu 2 kartus per mėnesį tręšti sukulentinių augalų trąšomis.\n" +"\n" +
                                "Genėjimas: nebūtinas.\n" + "\n" +
                                "Persodinimas: kas 3 metus.",
                        "Mamiliarijos",
                        convertDrawableToByteArray(R.drawable.mamiliarija_albata)));



                for (Plants p : seed) {
                    // konvertuojame drawable į byte[]
                    byte[] data = p.getImage();
                    String filename = UUID.randomUUID().toString() + ".png";
                    StorageReference imgRef = imagesRef.child(filename);

                    imgRef.putBytes(data)
                            .addOnSuccessListener(u -> imgRef.getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                        // Sukuriame dokumentą
                                        Map<String,Object> map = new HashMap<>();
                                        map.put("name", p.getName());
                                        map.put("description", p.getDescription());
                                        map.put("category", p.getCategory());
                                        map.put("origin", p.getOrigin());
                                        map.put("imageUrl", uri.toString());
                                        map.put("timestamp", FieldValue.serverTimestamp());

                                        db.collection("plants")
                                                .add(map);
                                    })
                            );
                }
            }
        });
    }

    private void loadPlantsFromFirestore() {
        db.collection("plants")
                .orderBy("name", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(snap -> {
                    allPlants.clear();
                    for (DocumentSnapshot doc : snap.getDocuments()) {

                        Plants p = doc.toObject(Plants.class);
                        p.setIdW(doc.getId());
                        allPlants.add(p);
                    }
                    filterAndDisplayPlants("");
                });
    }

    private void filterAndDisplayPlants(String query) {
        plantsContainer.removeAllViews();
        char last = '\0';

        // Filtruojame, rikiuojame pačius Plants objektus pagal name
        List<Plants> filtered = allPlants.stream()
                .filter(p -> p.getName().toLowerCase().contains(query.toLowerCase()))
                .sorted(Comparator.comparing(Plants::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());

        for (Plants p : filtered) {
            char first = Character.toUpperCase(p.getName().charAt(0));
            if (first != last) {
                TextView header = new TextView(this);
                header.setText(String.valueOf(first));
                header.setTextSize(24);
                header.setTextColor(ContextCompat.getColor(this, R.color.text_brown));
                header.setTypeface(ResourcesCompat.getCachedFont(this, R.font.spectral_sc), Typeface.BOLD);
                plantsContainer.addView(header);
                last = first;
            }
            Button btn = new Button(this);
            btn.setText(p.getName());
            btn.setTextSize(20);
            btn.setTextColor(ContextCompat.getColor(this, R.color.text_brown));
            btn.setTypeface(ResourcesCompat.getCachedFont(this, R.font.spectral_sc));
            btn.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            btn.setBackgroundColor(Color.TRANSPARENT);
            btn.setOnClickListener(v -> {
                Intent i = new Intent(this, PlantDescription.class);
                i.putExtra("plantId", p.getIdW());   // <- naudojame dokumento ID
                startActivity(i);
            });
            plantsContainer.addView(btn);
        }
    }

    private byte[] convertDrawableToByteArray(int drawableId) {
        Drawable d = getResources().getDrawable(drawableId, null);
        Bitmap bmp = d instanceof BitmapDrawable
                ? ((BitmapDrawable)d).getBitmap()
                : Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        if (!(d instanceof BitmapDrawable)) {
            Canvas c = new Canvas(bmp);
            d.setBounds(0,0,c.getWidth(),c.getHeight());
            d.draw(c);
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }
}