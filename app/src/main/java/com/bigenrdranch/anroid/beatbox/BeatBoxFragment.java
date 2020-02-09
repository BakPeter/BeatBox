package com.bigenrdranch.anroid.beatbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigenrdranch.anroid.beatbox.databinding.FragmentBeatBoxBinding;
import com.bigenrdranch.anroid.beatbox.databinding.ListItemSoundBinding;

import java.util.List;

public class BeatBoxFragment extends Fragment {
    private static final int DEFAULT_PLAYBACK_SPEED = 35;

    private BeatBox mBeatBox;

    public static BeatBoxFragment newInstance() {
        return new BeatBoxFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mBeatBox = new BeatBox(getActivity());
        mBeatBox.setRate(DEFAULT_PLAYBACK_SPEED / 100f);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentBeatBoxBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_beat_box, container, false);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.recyclerView.setAdapter(new SoundAdapter(mBeatBox.getSounds()));

        updateTextViewSpeedShower(binding, 0);

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTextViewSpeedShower(binding, progress);
                float rate = (DEFAULT_PLAYBACK_SPEED + progress) / 100f;
                mBeatBox.setRate(rate);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return binding.getRoot();
    }

    private void updateTextViewSpeedShower(FragmentBeatBoxBinding binding, int progress) {
        String speedText = getString(R.string.text_view_speed_shower_text, DEFAULT_PLAYBACK_SPEED + progress);
        binding.textViewSpeedShower.setText(speedText);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBeatBox.release();
    }

    private class SoundHolder extends RecyclerView.ViewHolder {
        private ListItemSoundBinding mBinding;

        private SoundHolder(ListItemSoundBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setViewModel(new SoundViewModel(mBeatBox));
        }

        public void bind(Sound sound) {
            mBinding.getViewModel().setSound(sound);
            mBinding.executePendingBindings();
        }
    }

    public class SoundAdapter extends RecyclerView.Adapter<SoundHolder> {
        private List<Sound> mSounds;

        public SoundAdapter(List<Sound> sounds) {
            mSounds = sounds;
        }

        @NonNull
        @Override
        public SoundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            ListItemSoundBinding binding = DataBindingUtil
                    .inflate(inflater, R.layout.list_item_sound, parent, false);

            return new SoundHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull SoundHolder holder, int position) {
            Sound sound = mSounds.get(position);
            holder.bind(sound);

        }

        @Override
        public int getItemCount() {
            return mSounds.size();
        }
    }
}
