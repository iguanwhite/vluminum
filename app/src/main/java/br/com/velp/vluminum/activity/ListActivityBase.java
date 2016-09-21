package br.com.velp.vluminum.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import br.com.velp.vluminum.R;

/**
 * Classe abstrata responsável por conter os atributos comuns de uma ListActivity.
 *
 * Created by Bruno Leonardo on 06/12/2014.
 */
public abstract class ListActivityBase extends ListActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_padrao, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                startActivity(new Intent(this, DashboardActivity_.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
