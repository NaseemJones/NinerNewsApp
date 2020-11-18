package com.t15.ninernewsnet.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.t15.ninernewsnet.CustomAdapter;
import com.t15.ninernewsnet.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final CardView cardView = root.findViewById(R.id.itemCard);
        RecyclerView recyclerView = root.findViewById(R.id.recycleViewLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Test information that will be shown on the main feed page
        ArrayList<String> textData = new ArrayList<>();
        textData.add("Megan Bird, a UNC Charlotte senior from Charlottesville, Virginia, is among finalists in contention for the Rhodes Scholarship, one of the world’s most prestigious graduate fellowships.");
        textData.add("#GivingTuesday, observed on Dec. 1, is a global generosity movement unleashing the power of people and organizations to transform their communities and the world. This year, UNC Charlotte is supporting the Jamil Niner Student Pantry.");
        textData.add("A Belk College of Business researcher is examining the price large pharmaceutical firms pay for their illegal practices.");
        textData.add("Significant progress is being made on two construction projects that will have a huge impact on the UNC Charlotte campus — the new Science building and the UNC Charlotte Marriott Hotel and Conference Center.");
        textData.add("November is National Entrepreneurship Month, and in today’s socially distanced world, recent surveys indicate social media is even more important to small business success. Belk College of Business researcher Nima Jalali offers some Twitter tips for entrepreneurs.");
        textData.add("Students at the UNC Charlotte School of Social Work are getting a first-hand look at how voting and democracy relate to their field of study thanks to a new internship. ");
        textData.add("UNC Charlotte recently received a National Science Foundation $500,000 Innovations in Graduate Education grant to support doctoral students in STEM disciplines as they develop their capstone thesis projects for patenting instead of for journal publications as has been the norm.");
        textData.add("Love and kindness go a long way when we and those around us are navigating challenging times. In many ways, Niner Nation has been inspiring kindness all year long.");
        textData.add("It’s flu season, and medical and public health professionals across the country are bracing for the potential of continued issues with COVID-19 overlapping with a flu outbreak to create what some are calling a “twindemic.”");
        textData.add("A new comprehensive study from UNC Charlotte’s Urban Institute, College of Health and Human Services and School of Social Work shows an effective approach to ending chronic homelessness that helps those in need and benefits communities. ");
        textData.add("Charlotte 49ers redshirt senior defensive end Tyriq Harris is one of 12 finalists for the prestigious National Football Foundation’s 2020 William V. Campbell Trophy, presented by Mazda.");
        textData.add("As the U.S. military develops the sophisticated weapon systems of the future, UNC Charlotte engineering students, including veteran Jason Solomon, are on the forefront of cutting-edge research in the field.");
        textData.add("The 2020 U.S. presidential election results are dominating the national conversation. What can be learned from the animal kingdom about how animal societies make decisions and resolve conflict?");

        CustomAdapter myAdapter = new CustomAdapter(textData);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return root;
    }
}