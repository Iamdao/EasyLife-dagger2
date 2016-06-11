package d.dao.easylife.dagger2.modules;

import d.dao.easylife.dagger2.api.ApiService;
import d.dao.easylife.dagger2.presenter.impl.JokeFirstPresenter;
import d.dao.easylife.dagger2.scopes.ActivityScope;
import d.dao.easylife.dagger2.ui.fragment.PageFragment;
import d.dao.easylife.dagger2.ui.view.IJokeFirstView;
import d.dao.easylife.dagger2.utils.ReservoirUtils;
import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dao on 6/10/16.
 */
@Module
public class JokeFirstFragmentModule {
    private PageFragment mPageFragment;

    public JokeFirstFragmentModule(PageFragment pageFragment) {
        this.mPageFragment = pageFragment;
    }

    @Provides
    @ActivityScope
    IJokeFirstView provideWeatherActivity() {
        return this.mPageFragment;
    }

    @Provides
    @ActivityScope
    JokeFirstPresenter provideJokeFirstPresenter(IJokeFirstView jokeFirstView,
                                                 ApiService apiService,
                                                 CompositeSubscription compositeSubscription,
                                                 ReservoirUtils reservoirUtils
    ) {
        return new JokeFirstPresenter(jokeFirstView, apiService,
                compositeSubscription, reservoirUtils
        );
    }

}
