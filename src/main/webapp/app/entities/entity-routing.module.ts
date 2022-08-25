import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'squeaks',
        data: { pageTitle: 'squeakerApp.squeaks.home.title' },
        loadChildren: () => import('./squeaks/squeaks.module').then(m => m.SqueaksModule),
      },
      {
        path: 'tags',
        data: { pageTitle: 'squeakerApp.tags.home.title' },
        loadChildren: () => import('./tags/tags.module').then(m => m.TagsModule),
      },
      {
        path: 'mentions',
        data: { pageTitle: 'squeakerApp.mentions.home.title' },
        loadChildren: () => import('./mentions/mentions.module').then(m => m.MentionsModule),
      },
      {
        path: 'utilizer',
        data: { pageTitle: 'squeakerApp.utilizer.home.title' },
        loadChildren: () => import('./utilizer/utilizer.module').then(m => m.UtilizerModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
