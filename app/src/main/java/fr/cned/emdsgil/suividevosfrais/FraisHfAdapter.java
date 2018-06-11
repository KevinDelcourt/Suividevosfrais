package fr.cned.emdsgil.suividevosfrais;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Locale;

class FraisHfAdapter extends BaseAdapter {

	private final ArrayList<FraisHf> lesFrais ; // liste des frais du mois
	private final LayoutInflater inflater ;
	private int key = 0;
    /**
	 * Constructeur de l'adapter pour valoriser les propriétés
     * @param context Accès au contexte de l'application
     * @param lesFrais Liste des frais hors forfait
	 * @param int key Clé du mois sélectionné (aaaamm)
	 *
	 * */
	public FraisHfAdapter(Context context, ArrayList<FraisHf> lesFrais,int key) {
		inflater = LayoutInflater.from(context) ;
		this.lesFrais = lesFrais ;
		this.key = key;
    }
	
	/**
	 * retourne le nombre d'éléments de la listview
	 */
	@Override
	public int getCount() {
		return lesFrais.size() ;
	}

	/**
	 * retourne l'item de la listview à un index précis
	 */
	@Override
	public Object getItem(int index) {
		return lesFrais.get(index) ;
	}

	/**
	 * retourne l'index de l'élément actuel
	 */
	@Override
	public long getItemId(int index) {
		return index;
	}

	/**
	 * structure contenant les éléments d'une ligne
	 */
	private class ViewHolder {
		TextView txtListJour ;
		TextView txtListMontant ;
		TextView txtListMotif ;
		ImageButton imgSupp;
	}
	
	/**
	 * Affichage dans la liste
	 */
	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		ViewHolder holder ;
		if (convertView == null) {
			holder = new ViewHolder() ;
			convertView = inflater.inflate(R.layout.layout_liste, parent, false) ;
			holder.txtListJour = convertView.findViewById(R.id.txtListJour);
			holder.txtListMontant = convertView.findViewById(R.id.txtListMontant);
			holder.txtListMotif = convertView.findViewById(R.id.txtListMotif);
			holder.imgSupp = convertView.findViewById(R.id.cmdSuppHf) ;

			convertView.setTag(holder) ;
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.txtListJour.setText(String.format(Locale.FRANCE, "%d", lesFrais.get(index).getJour()));
		holder.txtListMontant.setText(String.format(Locale.FRANCE, "%.2f", lesFrais.get(index).getMontant())) ;
		holder.txtListMotif.setText(lesFrais.get(index).getMotif()) ;

		holder.imgSupp.setId(index);
		holder.imgSupp.setClickable(true);
		holder.imgSupp.setOnClickListener(new imageViewClickListener(index));


		return convertView ;
	}

	/**
	 * Action du bouton supprimer d'un item de la liste
	 *
	 *
	 */
	private class imageViewClickListener implements View.OnClickListener {
		int position;

		public imageViewClickListener( int pos) {
			this.position = pos;
		}

		public void onClick(View v) {

			//On supprime le frais hors forfait de la liste vue
			lesFrais.remove(getItem(this.position));

			//On réactualise la liste dans la vue
			FraisHfAdapter.this.notifyDataSetChanged();

			//On met à jour la variable globale des frais

			if (Global.listFraisMois.containsKey(key)) {
				Global.listFraisMois.get(key).supprFraisHf(this.position);

				Serializer.serialize(Global.listFraisMois, inflater.getContext()) ;
			}
		}
	}
}
