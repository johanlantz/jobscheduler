package com.example.jobscheduler_ctsd;

import android.app.job.JobInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.jobscheduler_ctsd.databinding.FragmentFirstBinding;

import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<JobInfo> jobs = JobSchedulerManager.getPendingJobs(getContext());
        if (jobs.isEmpty()) {
            binding.buttonFirst.setText("Start Jobscheduler");
        } else {
            JobInfo job= jobs.get(0);
            binding.textviewFirst.setText("Job info\nisPersisted: " + job.isPersisted() + "\nisPeriodic: " + job.isPeriodic() + "\nInterval: " + job.getIntervalMillis()/1000);
            binding.buttonFirst.setText("Jobscheduler active - Press to restart");
        }


        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JobSchedulerManager.scheduleJobs(getContext());
                NavHostFragment.findNavController(FirstFragment.this).popBackStack();
                NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}