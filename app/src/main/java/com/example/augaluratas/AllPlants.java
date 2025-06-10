package com.example.augaluratas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Intent;
import android.widget.Toast;

public class AllPlants extends BaseActivity {
    private LinearLayout plantsContainer;
    private PlantsDatabase database;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private SearchView searchbar;
    private NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_plants);

        plantsContainer = findViewById(R.id.plantsContainer);
        database = PlantsDatabase.getDatabase(this);
        searchbar = findViewById(R.id.all_plants_searchbar);
        scrollView = findViewById(R.id.all_plants_scrollview);
        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadPlants();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    loadPlants();
                }
                return false;
            }
        });
        searchbar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                loadPlants();
                return false;
            }
        });
        //Remove focus from searchbar
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchbar.clearFocus();
                return false;
            }
        });
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bananmedis);
        if (bitmap == null) {
            Log.e("BitmapError", "Nepavyko užkrauti lotus_icon");
            return;
        }
        byte[] imageBytes = ImageUtils.bitmapToByteArray(bitmap);
        executorService.execute(() -> {
            if(database.plantsDAO().getAllPlants().isEmpty()){
                database.plantsDAO().insert(new Plants(
                        "Chlorofilas Variegatum",
                        "Labai nereikli kambarinė gėlė, priklausanti lelijinių šeimai. Išsiskiria ilgais lancetiškais lapais, kurie būna margi arba vienspalviai. Žiedai nėra labai dekoratyvūs, auginama dėl lapijos. Stipriai valo orą.\n" +"\n" +
                                "Laikymas: pakenčia sausą kambario orą.\n" + "\n" +
                                "Apšvietimas: gerai auga šviesioje patalpoje, tačiau toleruoja ir dalinį pavėsį. Margalapėms veislėms reikia daugiau šviesos nei žalialapėms.\n" +
                                "Temperatūra: 15–18 °C, vasarą galima auginti lauke.\n" +"\n" +
                                "Laistymas: reguliarus, substratas nuolat drėgnas, tačiau nepalaisčius nežus, tik apdžius lapų pakraščiai.\n" +"\n" +
                                "Substratas: derlingas, laidus. Nepakenčia sunkios, suslėgtos žemės.\n" +"\n" +
                                "Genėjimas: nereikalingas.\n" +"\n" +
                                "Tręšimas: kas 2–3 savaites skystomis trąšomis.\n" +"\n" +
                                "Persodinimas: kas 1–2 metus, geriausia pavasarį.",
                        "Chlorofitai",
                        convertDrawableToByteArray(R.drawable.chlorofilas_variegatum)));
                database.plantsDAO().insert(new Plants(
                        "Monstera Obliqua Peru",
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
                        convertDrawableToByteArray(R.drawable.monstera_obliqua_peru)));
                database.plantsDAO().insert(new Plants(
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
                database.plantsDAO().insert(new Plants(
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
                database.plantsDAO().insert(new Plants(
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

                database.plantsDAO().insert(new Plants(
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
                database.plantsDAO().insert(new Plants(
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
                database.plantsDAO().insert(new Plants(
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
                database.plantsDAO().insert(new Plants(
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
                database.plantsDAO().insert(new Plants(
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

                database.plantsDAO().insert(new Plants(
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
                database.plantsDAO().insert(new Plants(
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
                database.plantsDAO().insert(new Plants(
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
                database.plantsDAO().insert(new Plants(
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
                database.plantsDAO().insert(new Plants(
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

                database.plantsDAO().insert(new Plants(
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
                database.plantsDAO().insert(new Plants(
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
                database.plantsDAO().insert(new Plants(
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
                database.plantsDAO().insert(new Plants(
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
                database.plantsDAO().insert(new Plants(
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

            }
        });
        loadPlants();

        ImageButton menu = findViewById(R.id.sidebar_from_all_plants);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MeniuOverlay.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left, 0);
            }
        });
    }
    private void loadPlants() {
        executorService.execute(() -> {
            // Užklausa į duomenų bazę
            List<Plants> plantList = database.plantsDAO().getAllPlants();
            if (plantList == null || plantList.isEmpty()) return;

            // Naudojame LinkedHashSet, kad pašalintume pasikartojančius pavadinimus
            Set<String> uniquePlantNames = new LinkedHashSet<>();
            for (Plants plant : plantList) {
                uniquePlantNames.add(plant.getName());
            }

            // Paverčiame atgal į sąrašą ir surūšiuojame pagal pavadinimą
            List<String> sortedPlantNames = new ArrayList<>(uniquePlantNames);
            Collections.sort(sortedPlantNames, String.CASE_INSENSITIVE_ORDER);

            // Atliksime UI atnaujinimus pagrindiniame gije
            runOnUiThread(() -> {
                plantsContainer.removeAllViews(); // Išvalome senus duomenis
                char lastLetter = '\0'; // Laikome paskutinę įtrauktą raidę

                for (String plantName : sortedPlantNames) {

                    if (plantName.toLowerCase().contains(searchbar.getQuery().toString().toLowerCase())){
                        char firstLetter = Character.toUpperCase(plantName.charAt(0));
                        // Pridėti naują raidės antraštę, jei ši raidė dar nebuvo pridėta
                        if (firstLetter != lastLetter) {
                            TextView letterHeader = new TextView(this);
                            letterHeader.setText(String.valueOf(firstLetter));
                            letterHeader.setTextSize(24);
                            letterHeader.setTextColor(ContextCompat.getColor(this, R.color.text_brown));
                            letterHeader.setTypeface(ResourcesCompat.getCachedFont(this, R.font.spectral_sc), Typeface.BOLD);
                            plantsContainer.addView(letterHeader);
                            lastLetter = firstLetter;
                        }

                        // Sukuriame Button kiekvienam unikaliam augalui
                        Button plantButton = new Button(this);
                        plantButton.setText(plantName);
                        plantButton.setTextSize(20);
                        plantButton.setTextColor(ContextCompat.getColor(this, R.color.text_brown));
                        plantButton.setTypeface(ResourcesCompat.getCachedFont(this, R.font.spectral_sc));
                        plantButton.setBackgroundColor(Color.TRANSPARENT); // Be fono
                        plantButton.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        plantButton.setOnClickListener(v -> {
                            // Užklausa į duomenų bazę, kad gauti konkretaus augalo informaciją
                            executorService.execute(() -> {
                                Plants selectedPlant = database.plantsDAO().getPlantByName(plantName);
                                if (selectedPlant != null) {
                                    runOnUiThread(() -> {
                                        // Pereiti į kitą aktyvumą su pasirinktu augalu
                                        Intent intent = new Intent(getBaseContext(), PlantDescription.class);
                                        intent.putExtra("augalas", selectedPlant);
                                        startActivity(intent);
                                    });
                                }
                            });
                        });

                        plantsContainer.addView(plantButton);
                    }

                }

            });
        });
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
