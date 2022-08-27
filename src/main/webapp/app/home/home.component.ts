import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { GlobalServiceService } from '../shared/services/global-service.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;

  private readonly destroy$ = new Subject<void>();

  followers: any;

  constructor(private accountService: AccountService, private router: Router, private globalService: GlobalServiceService) {}

  ngOnInit(): void {
    this.globalService.apiCall(1).subscribe(data => {
      // look at data returned from the server
      console.log('data from server', data);

      // be careful about scope
      // if you need to save this value for later use, need to use a property
      this.followers = data?.utilizer?.followers;
    });

    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
