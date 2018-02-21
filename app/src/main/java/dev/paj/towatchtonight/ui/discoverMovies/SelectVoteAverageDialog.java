package dev.paj.towatchtonight.ui.discoverMovies;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.paj.towatchtonight.R;


public class SelectVoteAverageDialog extends DialogFragment {

    @BindView(R.id.dialog_select_vote_range_bar)
    RangeBar rangeBar;

    @BindView(R.id.dialog_select_vote_range_values)
    TextView rangeValues;

    @BindView(R.id.dialog_select_vote_average_select_button)
    Button selectButton;

    private int voteFrom = 1;
    private int voteTo = 10;
    private OnVoteSelectedListener mListener;
    private String parentFragmentTag;

    public static SelectVoteAverageDialog newInstance(String parentFragmentTag, int startingFrom, int startingTo) {
        SelectVoteAverageDialog f = new SelectVoteAverageDialog();
        Bundle args = new Bundle();
        args.putString("parentFragmentTag", parentFragmentTag);
        f.setArguments(args);

        f.voteFrom = startingFrom;
        f.voteTo = startingTo;

        return f;
    }

    public int getVoteFrom() {
        return voteFrom;
    }

    public int getVoteTo() {
        return voteTo;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_select_vote_avg_range, container, false);
        ButterKnife.bind(this, view);
        rangeValues.setText(getResources().getString(R.string.dialog_select_vote_range_values, voteFrom, voteTo));
        rangeBar.setOnRangeBarChangeListener((rangeBar, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue) -> {
            voteFrom = leftPinIndex + 1;
            voteTo = rightPinIndex + 1;
            rangeValues.setText(getResources().getString(R.string.dialog_select_vote_range_values, voteFrom, voteTo));
        });
        selectButton.setOnClickListener((button) -> this.dismiss());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentFragmentTag = getArguments().getString("parentFragmentTag");
        try {
            mListener = (OnVoteSelectedListener) ((AppCompatActivity)context).getSupportFragmentManager().findFragmentByTag(parentFragmentTag);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnVoteSelectedListener");
        }

    }

    @Override
    public void onDetach() {
        mListener.onVoteAverageSelected(this);
        super.onDetach();
    }

    interface OnVoteSelectedListener {
        void onVoteAverageSelected(SelectVoteAverageDialog dialog);
    }
}
