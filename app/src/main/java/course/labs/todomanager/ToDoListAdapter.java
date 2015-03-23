package course.labs.todomanager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import course.labs.todomanager.ToDoItem.Status;

public class ToDoListAdapter extends BaseAdapter {

	private final List<ToDoItem> mItems = new ArrayList<ToDoItem>();
	private final Context mContext;

	private static final String TAG = "Lab-UserInterface";

	public ToDoListAdapter(Context context) {

		mContext = context;

	}

	// Add a ToDoItem to the adapter
	// Notify observers that the data set has changed

	public void add(ToDoItem item) {

		mItems.add(item);
		notifyDataSetChanged();

	}

	// Clears the list adapter of all items.

	public void clear() {

		mItems.clear();
		notifyDataSetChanged();

	}

	// Returns the number of ToDoItems

	@Override
	public int getCount() {

		return mItems.size();

	}

	// Retrieve the number of ToDoItems

	@Override
	public Object getItem(int pos) {

		return mItems.get(pos);

	}

	// Get the ID for the ToDoItem
	// In this case it's just the position

	@Override
	public long getItemId(int pos) {

		return pos;

	}

	// TODO - Create a View for the ToDoItem at specified position
    // Remember to check whether convertView holds an already allocated View
    // before created a new View.
    // Consider using the ViewHolder pattern to make scrolling more efficient
    // See: http://developer.android.com/training/improving-layouts/smooth-scrolling.html

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        final View row;
        ViewHolder holder;

		// TODO - Get the current ToDoItem
		final ToDoItem toDoItem = (ToDoItem) getItem(position);


		// TODO - Inflate the View for this ToDoItem
		// from todo_item.xml.
        if (convertView==null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.todo_item, null);

            holder = new ViewHolder();
            holder.dateView = (TextView) row.findViewById(R.id.dateView);
            holder.priorityView = (TextView) row.findViewById(R.id.priorityView);
            holder.statusView = (CheckBox) row.findViewById(R.id.statusCheckBox);
            holder.titleView = (TextView) row.findViewById(R.id.titleView);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();

            // clear out old data (not sure why since we're going to overwrite it...)
            holder.statusView.setChecked(false);
            holder.titleView.setText("");
            holder.priorityView.setText("");
            holder.dateView.setText("");
        }

		// TODO - Fill in specific ToDoItem data
		// Remember that the data that goes in this View
		// corresponds to the user interface elements defined
		// in the layout file



		// TODO - Display Title in TextView
        TextView title = holder.titleView;
        title.setText(toDoItem.getTitle());

		// TODO - Set up Status CheckBox

        final CheckBox statusView = holder.statusView;
        statusView.setChecked(toDoItem.getStatus()==Status.DONE);
        
        // TODO - Must also set up an OnCheckedChangeListener,
        // which is called when the user toggles the status checkbox

        statusView
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Log.i(TAG,"Entered onCheckedChanged()");
                        if (statusView.isChecked()) {
                            toDoItem.setStatus(Status.DONE);
                        } else {
                            toDoItem.setStatus(Status.NOTDONE);
                        }
                        setToDoBackground(toDoItem.getDate(), row, statusView.isChecked());
					}
				});

		// TODO - Display Priority in a TextView
        TextView priorityView = holder.priorityView;
        priorityView.setText(toDoItem.getPriority().toString());


		// TODO - Display Time and Date
        TextView dateView = holder.dateView;
        // format the date&time to pass the stupid test...

        dateView.setText(toDoItem.FORMAT.format(toDoItem.getDate()));

        // set background color based on the date and check status
        setToDoBackground(toDoItem.getDate(), row, statusView.isChecked());
        // Return the View you just created
		return row;

	}

    private void setToDoBackground(Date todoDate, View view, boolean done) {
        Calendar now = Calendar.getInstance();
        Calendar toDoCal = Calendar.getInstance();

        now.add(now.DATE, 3);
        toDoCal.setTime(todoDate);
        if (done) {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.dullgreen));
        } else if (now.after(toDoCal)) {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.dullred));
        } else {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.black));
        }
    }

    private static class ViewHolder {
        public TextView titleView;
        public TextView priorityView;
        public TextView dateView;
        public CheckBox statusView;
    }
}
