import { AuthButton } from '@/components/AuthButton';
import { Page } from '@/components/PageLayout';
import type { ReactNode } from 'react';

export default function Home() {
  return (
    <Page className="bg-black text-white">
      <Page.Main className="!p-0 flex flex-col md:flex-row min-h-dvh">
        <section className="flex-1 flex items-center justify-center px-8 py-20 border-b border-white/15 md:border-b-0 md:border-r">
          <div className="max-w-xl space-y-6 text-center md:text-left">
            <p className="text-sm uppercase tracking-[0.4em] text-white/60">
              Wallet-native community portal
            </p>
            <h1 className="text-5xl md:text-6xl font-semibold leading-none">
              World Community
            </h1>
            <p className="text-lg text-white/70">
              Connect your World App wallet to enter the curated social feed,
              post updates, and build reputation backed by on-chain proof—no
              emails or passwords required.
            </p>
          </div>
        </section>

        <section className="flex-1 flex items-center justify-center px-6 py-20 bg-white text-black">
          <div className="w-full max-w-sm rounded-3xl border border-black/10 shadow-2xl shadow-black/20 p-8 space-y-6">
            <div className="space-y-2">
              <p className="text-xs font-semibold tracking-[0.3em] text-black/50">
                Sign in securely
              </p>
              <h2 className="text-3xl font-semibold">Connect wallet</h2>
              <p className="text-sm text-black/60">
                We federate your wallet address with the World Community user
                profile so your posts, likes, and verification level stay synced
                across devices.
              </p>
            </div>

            <div className="rounded-2xl border border-black/10 bg-black/5 p-4">
              <p className="text-sm font-medium text-black mb-3">
                Third-party wallet extension
              </p>
              <p className="text-xs text-black/60 mb-4">
                Trigger Wallet Auth in World App or a compatible extension. We
                never ask for a password—your signature proves ownership of the
                address listed on-chain.
              </p>
              <AuthButton />
            </div>

            <div className="space-y-3 text-sm text-black/70">
              <FeatureItem title="Wallet address">
                Stored as <code className="font-mono">wallet_address</code> in
                the backend <code>users</code> entity.
              </FeatureItem>
              <FeatureItem title="Username">
                Mirrors your World App handle; update anytime and we upsert the
                user record.
              </FeatureItem>
              <FeatureItem title="Verification">
                Once verified, we persist <code>world_verified_level</code> so
                feeds can trust your status.
              </FeatureItem>
            </div>
          </div>
        </section>
      </Page.Main>
    </Page>
  );
}

const FeatureItem = ({
  title,
  children,
}: {
  title: string;
  children: ReactNode;
}) => (
  <div className="flex flex-col gap-1">
    <p className="text-xs uppercase tracking-[0.3em] text-black/50">
      {title}
    </p>
    <p className="text-sm text-black/80 leading-snug">{children}</p>
  </div>
);
