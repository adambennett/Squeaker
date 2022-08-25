import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMentions } from '../mentions.model';

@Component({
  selector: 'jhi-mentions-detail',
  templateUrl: './mentions-detail.component.html',
})
export class MentionsDetailComponent implements OnInit {
  mentions: IMentions | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mentions }) => {
      this.mentions = mentions;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
